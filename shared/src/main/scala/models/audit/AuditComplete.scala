package models.audit

import java.util.UUID

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import models.tag.Tag

object AuditComplete {
  implicit val jsonEncoder: Encoder[AuditComplete] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditComplete] = deriveDecoder
}

case class AuditComplete(id: UUID, msg: String, tags: Seq[Tag] = Seq.empty, inserted: Seq[AuditModelPk] = Seq.empty)
