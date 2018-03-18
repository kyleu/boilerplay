package models.rest

import util.JsonSerializers._

object RestHeader {
  implicit val jsonEncoder: Encoder[RestHeader] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestHeader] = deriveDecoder

  val contentType = "Content-Type"
  val accept = "Accept"
  val cookie = "Cookie"
  val host = "Host"
  val userAgent = "User-Agent"
  val contentLength = "Content-Length"

  val defaultHeaders = Seq(
    RestHeader("Connection", "close")
  )

  val order = Seq(contentType, accept, cookie, host, "*", userAgent, contentLength)
}

case class RestHeader(k: String, v: String) {
  override lazy val toString = s"$k: $v"
}
