/* Generated File */
package models.table.audit

import java.time.LocalDateTime
import java.util.UUID
import models.audit.Audit
import models.tag.Tag
import services.database.slick.SlickQueryService.imports._

object AuditTable {
  val query = TableQuery[AuditTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result
}

class AuditTable(tag: slick.lifted.Tag) extends Table[Audit](tag, "audit") {
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

  val modelPrimaryKey = primaryKey("pk_audit", id)

  override val * = (id, act, app, client, server, userId, tags, msg, started, completed) <> (
    (Audit.apply _).tupled,
    Audit.unapply
  )
}

