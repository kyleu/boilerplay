package models

import java.time.LocalDateTime

import util.JsonSerializers.Circe._

object RequestMessage {
  implicit val jsonEncoder: Encoder[RequestMessage] = deriveEncoder
  implicit val jsonDecoder: Decoder[RequestMessage] = deriveDecoder
}

sealed trait RequestMessage

case class MalformedRequest(reason: String, content: String) extends RequestMessage
case class GetVersion(v: String) extends RequestMessage
case class Ping(ts: LocalDateTime) extends RequestMessage
