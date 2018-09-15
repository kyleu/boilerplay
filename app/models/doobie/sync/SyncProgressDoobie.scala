/* Generated File */
package models.doobie.sync

import cats.data.NonEmptyList
import models.doobie.DoobieQueries
import models.sync.SyncProgress
import services.database.DoobieQueryService.Imports._

object SyncProgressDoobie extends DoobieQueries[SyncProgress]("sync_progress") {
  override protected val countFragment = fr"""select count(*) from "sync_progress""""
  override protected val selectFragment = fr"""select "key", "status", "message", "last_time" from "sync_progress""""

  override protected val columns = Seq("key", "status", "message", "last_time")
  override protected val searchColumns = Seq("key", "status", "message", "last_time")

  override protected def searchFragment(q: String) = {
    fr""""key"::text = $q or "status"::text = $q or "message"::text = $q or "last_time"::text = $q"""
  }

  def getByPrimaryKey(key: String) = (selectFragment ++ whereAnd(fr"key = $key")).query[Option[SyncProgress]].unique
  def getByPrimaryKeySeq(keySeq: NonEmptyList[String]) = (selectFragment ++ in(fr"key", keySeq)).query[SyncProgress].to[Seq]
}

