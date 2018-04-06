package models.audit

import util.JsonSerializers._

object AuditField {
  implicit val jsonEncoder: Encoder[AuditField] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditField] = deriveDecoder
}

final case class AuditField(k: String, o: Option[String], n: Option[String]) {
  override def toString = s"$k: ${o.getOrElse(util.NullUtils.str)} -> ${n.getOrElse(util.NullUtils.str)}"
}
