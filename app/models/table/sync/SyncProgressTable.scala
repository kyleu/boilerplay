/* Generated File */
package models.table.sync

import java.time.LocalDateTime
import services.database.SlickQueryService.imports._

object SyncProgressTable {
  val query = TableQuery[SyncProgressTable]

  def getByPrimaryKey(key: String) = query.filter(_.key === key).result.headOption
  def getByPrimaryKeySeq(keySeq: Seq[String]) = query.filter(_.key.inSet(keySeq)).result
}

class SyncProgressTable(tag: Tag) extends Table[models.sync.SyncProgress](tag, "sync_progress") {
  val key = column[String]("key")
  val status = column[String]("status")
  val message = column[String]("message")
  val lastTime = column[LocalDateTime]("last_time")

  val modelPrimaryKey = primaryKey("pk_sync_progress", key)

  override val * = (key, status, message, lastTime) <> (
    (models.sync.SyncProgress.apply _).tupled,
    models.sync.SyncProgress.unapply
  )
}

