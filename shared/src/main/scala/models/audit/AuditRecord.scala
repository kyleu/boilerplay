package models.audit

import java.util.UUID

import models.result.data.{DataField, DataFieldModel, DataSummary}
import util.JsonSerializers._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object AuditRecord {
  implicit val jsonEncoder: Encoder[AuditRecord] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditRecord] = deriveDecoder
}

@JSExportTopLevel(util.Config.projectId + ".AuditRecord")
case class AuditRecord(
    @JSExport id: UUID = UUID.randomUUID,
    @JSExport auditId: UUID = UUID.randomUUID,
    @JSExport t: String = "default",
    @JSExport pk: Seq[String] = Seq.empty,
    @JSExport changes: Seq[AuditField] = Nil
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
