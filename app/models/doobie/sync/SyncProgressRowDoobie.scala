/* Generated File */
package models.doobie.sync

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.sync.SyncProgressRow

object SyncProgressRowDoobie extends DoobieQueries[SyncProgressRow]("sync_progress") {
  override val countFragment = fr"""select count(*) from "sync_progress""""
  override val selectFragment = fr"""select "key", "status", "message", "last_time" from "sync_progress""""

  override val columns = Seq("key", "status", "message", "last_time")
  override val searchColumns = Seq("key", "status", "message", "last_time")

  override def searchFragment(q: String) = {
    fr""""key"::text = $q or "status"::text = $q or "message"::text = $q or "last_time"::text = $q"""
  }

  def getByPrimaryKey(key: String) = (selectFragment ++ whereAnd(fr"key = $key")).query[Option[SyncProgressRow]].unique
  def getByPrimaryKeySeq(keySeq: NonEmptyList[String]) = (selectFragment ++ in(fr"key", keySeq)).query[SyncProgressRow].to[Seq]
}
