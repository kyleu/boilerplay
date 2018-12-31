/* Generated File */
package models.doobie.user

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import java.util.UUID
import models.user.SystemUserRow

object SystemUserRowDoobie extends DoobieQueries[SystemUserRow]("system_users") {
  override val countFragment = fr"""select count(*) from "system_users""""
  override val selectFragment = fr"""select "id", "username", "provider", "key", "role", "created" from "system_users""""

  override val columns = Seq("id", "username", "provider", "key", "role", "created")
  override val searchColumns = Seq("id", "username", "provider", "key")

  override def searchFragment(q: String) = {
    fr""""id"::text = $q or "username"::text = $q or "provider"::text = $q or "key"::text = $q or "role"::text = $q or "created"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[SystemUserRow]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[SystemUserRow].to[Seq]
}
