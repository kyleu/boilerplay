package models.table.audit

import java.util.UUID

import io.circe.Json
import services.database.SlickQueryService.imports._

object AuditRecordTable {
  val query = TableQuery[AuditRecordTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result

  def getByAuditId(auditId: UUID) = query.filter(_.auditId === auditId).result
  def getByAuditIdSeq(auditIdSeq: Seq[UUID]) = query.filter(_.auditId.inSet(auditIdSeq)).result
}

class AuditRecordTable(tag: Tag) extends Table[models.audit.AuditRecord](tag, "audit_record") {
  val id = column[UUID]("id")
  val auditId = column[UUID]("audit_id")
  val t = column[String]("t")
  val pk = column[List[String]]("pk")
  val changes = column[Json]("changes")

  val modelPrimaryKey = primaryKey("pk_audit_record", id)

  override val * = (id, auditId, t, pk, changes) <> (
    (models.audit.AuditRecord.apply _).tupled,
    models.audit.AuditRecord.unapply
  )
}

