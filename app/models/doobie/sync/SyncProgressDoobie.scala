/* Generated File */
package models.doobie.sync

import cats.data.NonEmptyList
import models.sync.SyncProgress
import services.database.DoobieQueryService.Imports._

object SyncProgressDoobie {
  val countFragment = fr"select count(*) from sync_progress"
  val selectFragment = fr"select key, status, message, last_time from sync_progress"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[SyncProgress].to[Seq]

  def getByPrimaryKey(key: String) = (selectFragment ++ fr"where key = $key").query[Option[SyncProgress]].unique
  def getByPrimaryKeySeq(keySeq: NonEmptyList[String]) = (selectFragment ++ in(fr"key", keySeq)).query[SyncProgress].to[Seq]
}

