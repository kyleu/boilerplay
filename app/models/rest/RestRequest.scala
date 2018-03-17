package models.rest

import java.time.LocalDateTime

import util.JsonSerializers._

object RestRequest {
  implicit val jsonEncoder: Encoder[RestRequest] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestRequest] = deriveDecoder
  val default = RestRequest(name = "Default Request")
}

case class RestRequest(
    name: String,
    protocol: Protocol = Protocol.Http,
    domain: String = "localhost",
    port: Option[Int] = None,
    path: String = "/",
    method: Method = Method.Get,
    contentType: ContentType = ContentType.Json,
    accept: MimeType = MimeType.Json,
    body: Option[RequestBody] = None,
    source: String = "adhoc",
    author: String = "Unknown",
    created: LocalDateTime = util.DateUtils.now
) {
  lazy val url = s"$protocol://$domain${port.map(":" + _).getOrElse("")}$path"

  override lazy val toString = {
    val sb = new StringBuilder()
    def add(s: String) = sb.append(s + "\n")
    add(s"${method.toString.toUpperCase} $path HTTP/1.1")
    sb.toString
  }
}
