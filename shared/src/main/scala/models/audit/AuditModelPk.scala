package models.audit

import util.JsonSerializers._

object AuditModelPk {
  implicit val jsonEncoder: Encoder[AuditModelPk] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditModelPk] = deriveDecoder
}

case class AuditModelPk(t: String, pk: Seq[String])
