/* Generated File */
package models.audit

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.JsonSerializers._
import io.circe.Json
import java.util.UUID

object AuditRecordRow {
  implicit val jsonEncoder: Encoder[AuditRecordRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditRecordRow] = deriveDecoder

  def empty(id: UUID = UUID.randomUUID, auditId: UUID = UUID.randomUUID, t: String = "", pk: List[String] = List.empty, changes: Json = Json.obj()) = {
    AuditRecordRow(id, auditId, t, pk, changes)
  }
}

final case class AuditRecordRow(
    id: UUID,
    auditId: UUID,
    t: String,
    pk: List[String],
    changes: Json
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("auditId", Some(auditId.toString)),
    DataField("t", Some(t)),
    DataField("pk", Some("{ " + pk.mkString(", ") + " }")),
    DataField("changes", Some(changes.toString))
  )

  def toSummary = DataSummary(model = "auditRecordRow", pk = id.toString, title = s"t: $t, pk: $pk")
}
