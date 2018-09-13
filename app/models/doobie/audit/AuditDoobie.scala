/* Generated File */
package models.doobie.audit

import cats.data.NonEmptyList
import java.util.UUID
import models.audit.Audit
import services.database.DoobieQueryService.Imports._

object AuditDoobie {
  val countFragment = fr"select count(*) from audit"
  val selectFragment = fr"select id, act, app, client, server, user_id, tags, msg, started, completed from audit"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[Audit].to[Seq]

  def getByPrimaryKey(id: UUID) = (selectFragment ++ fr"where id = $id").query[Option[Audit]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[Audit].to[Seq]
}

