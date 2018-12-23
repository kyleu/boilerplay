/* Generated File */
package models.table.audit

import io.circe.Json
import java.util.UUID
import models.audit.AuditRecordRow
import services.database.slick.SlickQueryService.imports._

object AuditRecordRowTable {
  val query = TableQuery[AuditRecordRowTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result

  def getByAuditId(auditId: UUID) = query.filter(_.auditId === auditId).result
  def getByAuditIdSeq(auditIdSeq: Seq[UUID]) = query.filter(_.auditId.inSet(auditIdSeq)).result
}

class AuditRecordRowTable(tag: slick.lifted.Tag) extends Table[AuditRecordRow](tag, "audit_record") {
  val id = column[UUID]("id", O.PrimaryKey)
  val auditId = column[UUID]("audit_id")
  val t = column[String]("t")
  val pk = column[List[String]]("pk")
  val changes = column[Json]("changes")

  override val * = (id, auditId, t, pk, changes) <> (
    (AuditRecordRow.apply _).tupled,
    AuditRecordRow.unapply
  )
}

