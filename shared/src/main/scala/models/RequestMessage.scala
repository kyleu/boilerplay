package models

import java.time.LocalDateTime

import util.JsonSerializers._

sealed trait RequestMessage

object RequestMessage {
  implicit val jsonEncoder: Encoder[RequestMessage] = deriveEncoder
  implicit val jsonDecoder: Decoder[RequestMessage] = deriveDecoder

  case class MalformedRequest(reason: String, content: String) extends RequestMessage
  case class GetVersion(v: String) extends RequestMessage
  case class Ping(ts: LocalDateTime) extends RequestMessage
}
