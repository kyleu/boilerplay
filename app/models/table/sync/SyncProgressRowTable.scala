/* Generated File */
package models.table.sync

import java.time.LocalDateTime
import models.sync.SyncProgressRow
import services.database.slick.SlickQueryService.imports._

object SyncProgressRowTable {
  val query = TableQuery[SyncProgressRowTable]

  def getByPrimaryKey(key: String) = query.filter(_.key === key).result.headOption
  def getByPrimaryKeySeq(keySeq: Seq[String]) = query.filter(_.key.inSet(keySeq)).result
}

class SyncProgressRowTable(tag: slick.lifted.Tag) extends Table[SyncProgressRow](tag, "sync_progress") {
  val key = column[String]("key", O.PrimaryKey)
  val status = column[String]("status")
  val message = column[String]("message")
  val lastTime = column[LocalDateTime]("last_time")

  override val * = (key, status, message, lastTime) <> (
    (SyncProgressRow.apply _).tupled,
    SyncProgressRow.unapply
  )
}

