package models.audit

import models.tag.Tag
import util.JsonSerializers.Circe._

object AuditStart {
  implicit val jsonEncoder: Encoder[AuditStart] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditStart] = deriveDecoder
}

case class AuditStart(
    action: String,
    app: Option[String] = None,
    client: String = "default",
    tags: Seq[Tag] = Seq.empty,
    models: Seq[AuditModelPk] = Seq.empty
)

