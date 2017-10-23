package models.audit

import java.util.UUID

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

object AuditComplete {
  implicit val jsonEncoder: Encoder[AuditComplete] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditComplete] = deriveDecoder
}

case class AuditComplete(id: UUID, msg: String, tags: Map[String, String] = Map.empty, inserted: Seq[AuditModelPk] = Seq.empty)
