/* Generated File */
package models.table.auth

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.LocalDateTime
import java.util.UUID
import models.auth.SystemUserRow

object SystemUserRowTable {
  val query = TableQuery[SystemUserRowTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result
}

class SystemUserRowTable(tag: slick.lifted.Tag) extends Table[SystemUserRow](tag, "system_user") {
  val id = column[UUID]("id", O.PrimaryKey)
  val username = column[Option[String]]("username")
  val provider = column[String]("provider")
  val key = column[String]("key")
  val role = column[String]("role")
  val created = column[LocalDateTime]("created")

  override val * = (id, username, provider, key, role, created) <> (
    (SystemUserRow.apply _).tupled,
    SystemUserRow.unapply
  )
}

