package models

import java.time.LocalDateTime
import java.util.UUID

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import io.circe.{Decoder, Encoder}

import models.audit.{Audit, AuditComplete, AuditStart}
import models.user.UserPreferences

object ResponseMessage {
  implicit val jsonEncoder: Encoder[ResponseMessage] = deriveEncoder
  implicit val jsonDecoder: Decoder[ResponseMessage] = deriveDecoder
}

sealed trait ResponseMessage

case class ServerError(reason: String, content: String) extends ResponseMessage
case class VersionResponse(version: String) extends ResponseMessage

case class Pong(ts: LocalDateTime) extends ResponseMessage
case class Disconnected(reason: String) extends ResponseMessage

case class UserSettings(userId: UUID, username: String, email: String, preferences: UserPreferences) extends ResponseMessage

case class AuditStartNotification(id: UUID, as: AuditStart) extends ResponseMessage
case class AuditCompleteNotification(ac: AuditComplete) extends ResponseMessage
case class AuditNotification(a: Audit) extends ResponseMessage
