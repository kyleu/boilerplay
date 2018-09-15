/* Generated File */
package models.doobie.audit

import cats.data.NonEmptyList
import java.util.UUID
import models.audit.Audit
import models.doobie.DoobieQueries
import services.database.DoobieQueryService.Imports._

object AuditDoobie extends DoobieQueries[Audit]("audit") {
  override protected val countFragment = fr"""select count(*) from "audit""""
  override protected val selectFragment = fr"""select "id", "act", "app", "client", "server", "user_id", "tags", "msg", "started", "completed" from "audit""""

  override protected val columns = Seq("id", "act", "app", "client", "server", "user_id", "tags", "msg", "started", "completed")
  override protected val searchColumns = Seq("id", "act", "app", "client", "server", "user_id", "tags")

  override protected def searchFragment(q: String) = {
    fr""""id"::text = $q or "act"::text = $q or "app"::text = $q or "client"::text = $q or "server"::text = $q or "user_id"::text = $q or "tags"::text = $q or "msg"::text = $q or "started"::text = $q or "completed"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[Audit]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[Audit].to[Seq]
}

