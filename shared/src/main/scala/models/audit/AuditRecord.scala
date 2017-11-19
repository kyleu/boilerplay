package models.audit

import java.util.UUID

import io.circe.{Decoder, Encoder}
import models.result.data.{DataField, DataFieldModel, DataSummary}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object AuditRecord {
  implicit val jsonEncoder: Encoder[AuditRecord] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditRecord] = deriveDecoder
}

case class AuditRecord(
    id: UUID = UUID.randomUUID,
    auditId: UUID = UUID.randomUUID,
    t: String = "default",
    pk: Seq[String] = Seq.empty,
    changes: Seq[AuditField] = Nil
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("auditId", Some(auditId.toString)),
    DataField("t", Some(t.toString)),
    DataField("pk", Some(pk.toString)),
    DataField("changes", Some(changes.toString))
  )

  def toSummary = DataSummary(model = "auditRecord", pk = Seq(id.toString), title = s"$t / $pk ($id)")
}
