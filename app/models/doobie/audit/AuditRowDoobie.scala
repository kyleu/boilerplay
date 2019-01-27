/* Generated File */
package models.doobie.audit

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import java.util.UUID
import models.audit.AuditRow

object AuditRowDoobie extends DoobieQueries[AuditRow]("audit") {
  override val countFragment = fr"""select count(*) from "audit""""
  override val selectFragment = fr"""select "id", "act", "app", "client", "server", "user_id", "tags", "msg", "started", "completed" from "audit""""

  override val columns = Seq("id", "act", "app", "client", "server", "user_id", "tags", "msg", "started", "completed")
  override val searchColumns = Seq("id", "act", "app", "client", "server", "user_id")

  override def searchFragment(q: String) = {
    fr""""id"::text = $q or "act"::text = $q or "app"::text = $q or "client"::text = $q or "server"::text = $q or "user_id"::text = $q or "tags"::text = $q or "msg"::text = $q or "started"::text = $q or "completed"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[AuditRow]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[AuditRow].to[Seq]
}
