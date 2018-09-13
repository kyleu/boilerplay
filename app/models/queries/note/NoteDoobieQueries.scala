/* Generated File */
package models.queries.note

import java.util.UUID

import cats.data.NonEmptyList
import models.note.Note
import services.database.DoobieQueryService.Imports._

object NoteDoobieQueries {
  val countFragment = fr"select count(*) from note"
  val selectFragment = fr"select id, rel_type, rel_pk, text, author, created from note"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[models.note.Note].to[Seq]

  def getByPrimaryKey(id: UUID) = (selectFragment ++ fr"where id = $id").query[Option[Note]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[Option[Note]].unique

  def countByAuthor(author: UUID) = (countFragment ++ fr"where author = $author").query[Int].unique
  def getByAuthor(author: UUID) = (selectFragment ++ whereAnd(fr"author = $author")).query[Note].to[Seq]
  def getByAuthorSeq(authorSeq: NonEmptyList[UUID]) = (selectFragment ++ whereAnd(in(fr"author", authorSeq))).query[Note].to[Seq]

  def insert(model: Note) = {
    val q = fr"insert into note (id, rel_type, rel_pk, text, author, created) values (?, ?, ?, ?, ?, ?)"
    q.update.withUniqueGeneratedKeys[Note]("id", "rel_type", "rel_pk", "text", "author", "created")
  }

  def insertBatch(models: NonEmptyList[Note]) = {
    val sql = "insert into note (id, rel_type, rel_pk, text, author, created) values (?, ?, ?, ?, ?, ?)"
    Update[Note](sql).updateManyWithGeneratedKeys[Note]("id", "rel_type", "rel_pk", "text", "author", "created")(models)
  }

  def removeByPrimaryKey(id: UUID) = {
    fr"delete from note where id = $id".update.withUniqueGeneratedKeys[Note]("id", "rel_type", "rel_pk", "text", "author", "created")
  }
}
