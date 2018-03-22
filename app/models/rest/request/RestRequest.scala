package models.rest.request

import java.time.LocalDateTime

import models.rest.http._
import models.rest.http.{RestHeader => rh}
import util.JsonSerializers._

object RestRequest {
  implicit val jsonEncoder: Encoder[RestRequest] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestRequest] = deriveDecoder
}

case class RestRequest(
    name: String,
    folder: Option[String] = None,
    title: String,
    description: Option[String] = None,
    method: Method = Method.Get,
    url: RestUrl = RestUrl(),
    contentType: MimeType = MimeType.Json,
    charset: Option[String] = Some("utf-8"),
    accept: MimeType = MimeType.Json,
    userAgent: Option[UserAgent] = None,
    headers: Seq[RestHeader] = Nil,
    cookies: Seq[RestCookie] = Nil,
    body: Option[RestBody] = None,
    source: String = "adhoc",
    author: String = "Unknown",
    created: LocalDateTime = util.DateUtils.now
) {
  lazy val fileLocation = (folder.getOrElse("") + "/" + name).stripPrefix("/")

  lazy val finalHeaders = {
    def getHeader(k: String) = headers.find(_.k == k).map(_.v)
    rh.order.flatMap {
      case rh.contentType => Seq(RestHeader(rh.contentType, getHeader(rh.contentType).getOrElse(contentType.mt + charset.map("; charset=" + _).getOrElse(""))))
      case rh.accept => Seq(RestHeader(rh.accept, getHeader(rh.accept).getOrElse(contentType.mt)))
      case rh.cookie => RestCookie.headerFor(getHeader(rh.cookie).map(mergeCookies).getOrElse(cookies)).map(RestHeader(rh.cookie, _))
      case rh.host => Seq(RestHeader(rh.host, getHeader(rh.host).getOrElse(url.domain)))
      case rh.userAgent => getHeader(rh.userAgent).orElse(userAgent.map(_.v)).map(v => RestHeader(rh.userAgent, v)).toSeq
      case rh.contentLength => getHeader(rh.contentLength).orElse(body.map(_.bytes.length.toString)).map(v => RestHeader(rh.contentLength, v)).toSeq
      case "*" => headers.filterNot(h => rh.order.contains(h.k))
      case x => throw new IllegalStateException(s"Unhandled header order entry [$x].")
    }
  }

  private[this] def mergeCookies(s: String) = {
    val combined = RestCookie.cookiesFrom(s) ++ cookies
    val mapped = combined.groupBy(_.k).mapValues(_.head)
    combined.map(_.k).distinct.map(x => mapped.apply(x)).toSeq
  }
}
