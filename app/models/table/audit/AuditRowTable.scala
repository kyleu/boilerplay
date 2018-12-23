/* Generated File */
package models.table.audit

import java.time.LocalDateTime
import java.util.UUID
import models.audit.AuditRow
import models.tag.Tag
import services.database.slick.SlickQueryService.imports._

object AuditRowTable {
  val query = TableQuery[AuditRowTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result
}

class AuditRowTable(tag: slick.lifted.Tag) extends Table[AuditRow](tag, "audit") {
  val id = column[UUID]("id", O.PrimaryKey)
  val act = column[String]("act")
  val app = column[String]("app")
  val client = column[String]("client")
  val server = column[String]("server")
  val userId = column[UUID]("user_id")
  val tags = column[List[Tag]]("tags")
  val msg = column[String]("msg")
  val started = column[LocalDateTime]("started")
  val completed = column[LocalDateTime]("completed")

  override val * = (id, act, app, client, server, userId, tags, msg, started, completed) <> (
    (AuditRow.apply _).tupled,
    AuditRow.unapply
  )
}

