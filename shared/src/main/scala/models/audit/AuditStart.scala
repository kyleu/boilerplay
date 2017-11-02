package models.audit

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import models.tag.Tag

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

