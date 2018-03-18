package models.rest

import java.time.LocalDateTime

import models.rest.RequestBody.{BinaryContent, JsonContent, TextContent}
import models.rest.enums._
import util.JsonSerializers._

object RestRequest {
  implicit val jsonEncoder: Encoder[RestRequest] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestRequest] = deriveDecoder
}

case class RestRequest(
    name: String,
    title: String,
    protocol: Protocol = Protocol.Http,
    domain: String = "localhost",
    port: Option[Int] = None,
    path: String = "",
    method: Method = Method.Get,
    contentType: ContentType = ContentType.Json,
    accept: MimeType = MimeType.Json,
    userAgent: Option[UserAgent] = None,
    headers: Seq[RestHeader] = Nil,
    cookies: Seq[RestCookie] = Nil,
    body: Option[RequestBody] = None,
    source: String = "adhoc",
    author: String = "Unknown",
    created: LocalDateTime = util.DateUtils.now
) {
  lazy val fullPath = (path + "/" + name).stripPrefix("/")
  lazy val url = s"$protocol://$domain${port.map(":" + _).getOrElse("")}$path"

  lazy val finalHeaders = {
    import models.rest.{RestHeader => rh}
    def getHeader(k: String) = headers.find(_.k == k).map(_.v)
    rh.order.flatMap {
      case rh.contentType => Seq(RestHeader(rh.contentType, getHeader(rh.contentType).getOrElse(contentType.t)))
      case "*" => headers.filterNot(h => rh.order.contains(h.k))
      case rh.accept => Seq(RestHeader(rh.accept, getHeader(rh.accept).getOrElse(contentType.t)))
      case rh.cookie => RestCookie.headerFor(getHeader(rh.cookie).map(mergeCookies).getOrElse(cookies)).map(RestHeader(rh.cookie, _))
      case rh.host => Seq(RestHeader(rh.host, getHeader(rh.host).getOrElse(domain)))
      case rh.userAgent => getHeader(rh.userAgent).orElse(userAgent.map(_.v)).map(v => RestHeader(rh.userAgent, v)).toSeq
      case rh.contentLength => getHeader(rh.contentLength).orElse(body.map(_.bytes.length.toString)).map(v => RestHeader(rh.contentLength, v)).toSeq
      case x => throw new IllegalStateException(s"Unhandled header order entry [$x].")
    }
  }

  override lazy val toString = RestRequestSerializers.toRaw(this)

  private[this] def mergeCookies(s: String) = {
    val combined = RestCookie.cookiesFrom(s) ++ cookies
    val mapped = combined.groupBy(_.k).mapValues(_.head)
    combined.map(_.k).distinct.map(x => mapped.apply(x)).toSeq
  }
}
