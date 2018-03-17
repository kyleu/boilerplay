package models.audit

import java.util.UUID

import models.tag.Tag
import util.JsonSerializers.Circe._

object AuditComplete {
  implicit val jsonEncoder: Encoder[AuditComplete] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditComplete] = deriveDecoder
}

case class AuditComplete(id: UUID, msg: String, tags: Seq[Tag] = Seq.empty, inserted: Seq[AuditModelPk] = Seq.empty)
