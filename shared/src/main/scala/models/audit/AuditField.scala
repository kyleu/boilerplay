package models.audit

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

object AuditField {
  implicit val jsonEncoder: Encoder[AuditField] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditField] = deriveDecoder
}

case class AuditField(k: String, o: Option[String], n: Option[String])
