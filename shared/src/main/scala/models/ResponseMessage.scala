package models

import java.util.UUID

import util.JsonSerializers._

sealed trait ResponseMessage

object ResponseMessage {
  implicit val jsonEncoder: Encoder[ResponseMessage] = deriveEncoder
  implicit val jsonDecoder: Decoder[ResponseMessage] = deriveDecoder

  case class ServerError(reason: String, content: String) extends ResponseMessage
  case class VersionResponse(version: String) extends ResponseMessage

  case class Pong(ts: Long) extends ResponseMessage
  case class Disconnected(reason: String) extends ResponseMessage

  case class UserSettings(userId: UUID, username: String, email: String) extends ResponseMessage
}

