package models.audit

import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.util.NullUtils

object AuditField {
  implicit val jsonEncoder: Encoder[AuditField] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditField] = deriveDecoder
}

final case class AuditField(k: String, o: Option[String], n: Option[String]) {
  override def toString = s"$k: ${o.getOrElse(NullUtils.str)} -> ${n.getOrElse(NullUtils.str)}"
}
