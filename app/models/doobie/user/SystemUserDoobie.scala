/* Generated File */
package models.doobie.user

import cats.data.NonEmptyList
import java.util.UUID
import models.doobie.DoobieQueries
import models.user.SystemUser
import services.database.DoobieQueryService.Imports._

object SystemUserDoobie extends DoobieQueries[SystemUser]("system_users") {
  override protected val countFragment = fr"""select count(*) from "system_users""""
  override protected val selectFragment = fr"""select "id", "username", "provider", "key", "prefs", "role", "created" from "system_users""""

  override protected val columns = Seq("id", "username", "provider", "key", "prefs", "role", "created")
  override protected val searchColumns = Seq("id", "username", "provider", "key")

  override protected def searchFragment(q: String) = {
    fr""""id"::text = $q or "username"::text = $q or "provider"::text = $q or "key"::text = $q or "prefs"::text = $q or "role"::text = $q or "created"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[SystemUser]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[SystemUser].to[Seq]
}

