/* Generated File */
package models.doobie.user

import cats.data.NonEmptyList
import java.util.UUID
import models.user.SystemUser
import services.database.DoobieQueryService.Imports._

object SystemUserDoobie {
  val countFragment = fr"select count(*) from system_users"
  val selectFragment = fr"select id, username, provider, key, prefs, role, created from system_users"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[SystemUser].to[Seq]

  def getByPrimaryKey(id: UUID) = (selectFragment ++ fr"where id = $id").query[Option[SystemUser]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[SystemUser].to[Seq]
}

