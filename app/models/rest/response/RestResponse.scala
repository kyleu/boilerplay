package models.rest.response

import java.time.LocalDateTime

import models.rest.http._
import util.JsonSerializers._

object RestResponse {
  implicit val jsonEncoder: Encoder[RestResponse] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestResponse] = deriveDecoder
}

case class RestResponse(
    title: String,
    sourcePath: String = "tmp",
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
)
