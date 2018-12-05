/* Generated File */
package models.table.sync

import java.time.LocalDateTime
import models.sync.SyncProgress
import services.database.slick.SlickQueryService.imports._

object SyncProgressTable {
  val query = TableQuery[SyncProgressTable]

  def getByPrimaryKey(key: String) = query.filter(_.key === key).result.headOption
  def getByPrimaryKeySeq(keySeq: Seq[String]) = query.filter(_.key.inSet(keySeq)).result
}

class SyncProgressTable(tag: slick.lifted.Tag) extends Table[SyncProgress](tag, "sync_progress") {
  val key = column[String]("key", O.PrimaryKey)
  val status = column[String]("status")
  val message = column[String]("message")
  val lastTime = column[LocalDateTime]("last_time")

  override val * = (key, status, message, lastTime) <> (
    (SyncProgress.apply _).tupled,
    SyncProgress.unapply
  )
}

