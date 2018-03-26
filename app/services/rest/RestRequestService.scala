package services.rest

import java.net.ConnectException
import java.time.LocalDateTime

import models.rest.http._
import models.rest.request.RestRequest
import models.rest.response.RestResponse
import play.api.libs.ws.{WSClient, WSResponse}
import util.FutureUtils.serviceContext

import scala.concurrent.Future

@javax.inject.Singleton
class RestRequestService @javax.inject.Inject() (ws: WSClient) {
  def call(req: RestRequest, log: String => Unit) = {
    val bodySize = req.body.map(b => s", and a body of size [${b.size}].)")
    log(s"Calling [${req.url}] with method [${req.method}], content type [${req.contentType}}]$bodySize.")

    val started = util.DateUtils.now

    val wsr = ws.url(req.url.toString).withMethod(req.method.value).addHttpHeaders(req.headers.map(x => x.k -> x.v): _*)

    wsr.stream().map(parseResponse(req, started, _)).recoverWith {
      case c: ConnectException => Future.successful(RestResponse.forException(req, c))
    }
  }

  def parseResponse(req: RestRequest, started: LocalDateTime, rsp: WSResponse, logs: Seq[String] = Nil) = {
    val headers = rsp.headers.map(h => RestHeader(h._1, h._2.headOption.getOrElse(""))).toSeq
    val contentType = headers.find(_.k == RestHeader.contentType).map(x => MimeType.forString(x.v))
    val bodyBytes = rsp.bodyAsBytes
    val body = if (bodyBytes.isEmpty) {
      None
    } else {
      Some(RestBody.fromBytes(contentType, rsp.bodyAsBytes.toByteBuffer.array))
    }
    RestResponse(
      title = req.title,
      source = None,
      started = started,
      completed = util.DateUtils.now,

      url = req.url,
      status = rsp.status,
      statusText = rsp.statusText,
      contentType = contentType,
      headers = headers,
      cookies = Nil,
      body = body,

      logs = logs
    )
  }
}
