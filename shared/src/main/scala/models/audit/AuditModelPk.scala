package models.audit

import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object AuditModelPk {
  implicit val jsonEncoder: Encoder[AuditModelPk] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditModelPk] = deriveDecoder
}

case class AuditModelPk(t: String, pk: Seq[String])
