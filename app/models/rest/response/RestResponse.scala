package models.rest.response

import java.time.LocalDateTime

import models.rest.http._
import models.rest.request.RestRequest
import util.JsonSerializers._

object RestResponse {
  implicit val jsonEncoder: Encoder[RestResponse] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestResponse] = deriveDecoder

  def forException(req: RestRequest, ex: Throwable, logs: Seq[String] = Nil) = RestResponse(
    title = req.title,
    url = req.url,
    statusText = ex.getMessage,
    logs = logs :+ ("Error: " + ex.getClass.getSimpleName)
  )
}

case class RestResponse(
    title: String,
    source: Option[String] = None,
    startedBy: String = "Unknown",
    started: LocalDateTime = util.DateUtils.now,
    completed: LocalDateTime = util.DateUtils.now,

    url: RestUrl = RestUrl(),
    status: Int = 0,
    statusText: String = "Unknown",
    contentType: Option[MimeType] = None,
    headers: Seq[RestHeader] = Nil,
    cookies: Seq[RestCookie] = Nil,
    body: Option[RestBody] = None,

    logs: Seq[String] = Nil
) {
  val durationMs = (util.DateUtils.toMillis(completed) - util.DateUtils.toMillis(started)).toInt
}
