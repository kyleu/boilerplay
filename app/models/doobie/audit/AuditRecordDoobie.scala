/* Generated File */
package models.doobie.audit

import cats.data.NonEmptyList
import java.util.UUID
import models.audit.AuditRecord
import models.doobie.DoobieQueries
import services.database.DoobieQueryService.Imports._

object AuditRecordDoobie extends DoobieQueries[AuditRecord]("audit_record") {
  override val countFragment = fr"""select count(*) from "audit_record""""
  override val selectFragment = fr"""select "id", "audit_id", "t", "pk", "changes" from "audit_record""""

  override val columns = Seq("id", "audit_id", "t", "pk", "changes")
  override val searchColumns = Seq("id", "t", "pk")

  override def searchFragment(q: String) = {
    fr""""id"::text = $q or "audit_id"::text = $q or "t"::text = $q or "pk"::text = $q or "changes"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[AuditRecord]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[AuditRecord].to[Seq]

  def countByAuditId(auditId: UUID) = (countFragment ++ whereAnd(fr"auditId = $auditId")).query[Int].unique
  def getByAuditId(auditId: UUID) = (selectFragment ++ whereAnd(fr"auditId = $auditId")).query[AuditRecord].to[Seq]
  def getByAuditIdSeq(auditIdSeq: NonEmptyList[UUID]) = (selectFragment ++ whereAnd(in(fr"auditId", auditIdSeq))).query[AuditRecord].to[Seq]
}

