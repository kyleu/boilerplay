/* Generated File */
package models.table.note

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.LocalDateTime
import java.util.UUID
import models.note.NoteRow

object NoteRowTable {
  val query = TableQuery[NoteRowTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result

  def getByAuthor(author: UUID) = query.filter(_.author === author).result
  def getByAuthorSeq(authorSeq: Seq[UUID]) = query.filter(_.author.inSet(authorSeq)).result
}

class NoteRowTable(tag: slick.lifted.Tag) extends Table[NoteRow](tag, "note") {
  val id = column[UUID]("id", O.PrimaryKey)
  val relType = column[Option[String]]("rel_type")
  val relPk = column[Option[String]]("rel_pk")
  val text = column[String]("text")
  val author = column[UUID]("author")
  val created = column[LocalDateTime]("created")

  override val * = (id, relType, relPk, text, author, created) <> (
    (NoteRow.apply _).tupled,
    NoteRow.unapply
  )
}

