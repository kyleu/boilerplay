package models.supervisor

import java.time.LocalDateTime
import java.util.UUID

import util.JsonSerializers._

object SocketDescription {
  implicit val jsonEncoder: Encoder[SocketDescription] = deriveEncoder
  implicit val jsonDecoder: Decoder[SocketDescription] = deriveDecoder
}

final case class SocketDescription(socketId: UUID, userId: UUID, name: String, channel: String, started: LocalDateTime)
