/* Generated File */
package models.doobie.audit

import cats.data.NonEmptyList
import java.util.UUID
import models.audit.AuditRecord
import services.database.DoobieQueryService.Imports._

object AuditRecordDoobie {
  val countFragment = fr"select count(*) from audit_record"
  val selectFragment = fr"select id, audit_id, t, pk, changes from audit_record"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[AuditRecord].to[Seq]

  def getByPrimaryKey(id: UUID) = (selectFragment ++ fr"where id = $id").query[Option[AuditRecord]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[AuditRecord].to[Seq]

  def countByAuditId(auditId: UUID) = (countFragment ++ fr"where auditId = $auditId").query[Int].unique
  def getByAuditId(auditId: UUID) = (selectFragment ++ whereAnd(fr"auditId = $auditId")).query[AuditRecord].to[Seq]
  def getByAuditIdSeq(auditIdSeq: NonEmptyList[UUID]) = (selectFragment ++ whereAnd(in(fr"auditId", auditIdSeq))).query[AuditRecord].to[Seq]
}

