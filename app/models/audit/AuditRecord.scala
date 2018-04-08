/* Generated File */
package models.audit

import io.circe.Json
import java.util.UUID
import models.result.data.{DataField, DataFieldModel, DataSummary}
import util.JsonSerializers._

object AuditRecord {
  implicit val jsonEncoder: Encoder[AuditRecord] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditRecord] = deriveDecoder
}

final case class AuditRecord(
    id: UUID = UUID.randomUUID,
    auditId: UUID = UUID.randomUUID,
    t: String = "",
    pk: Seq[String] = Seq.empty,
    changes: Json = Json.obj()
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("auditId", Some(auditId.toString)),
    DataField("t", Some(t)),
    DataField("pk", Some(pk.toString)),
    DataField("changes", Some(changes.toString))
  )

  def toSummary = DataSummary(model = "auditRecord", pk = Seq(id.toString), title = s"$t / $pk ($id)")
}
