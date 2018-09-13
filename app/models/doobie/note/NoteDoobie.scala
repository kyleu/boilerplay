/* Generated File */
package models.doobie.note

import cats.data.NonEmptyList
import java.util.UUID
import models.note.Note
import services.database.DoobieQueryService.Imports._

object NoteDoobie {
  val countFragment = fr"select count(*) from note"
  val selectFragment = fr"select id, rel_type, rel_pk, text, author, created from note"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[Note].to[Seq]

  def getByPrimaryKey(id: UUID) = (selectFragment ++ fr"where id = $id").query[Option[Note]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[Note].to[Seq]

  def countByAuthor(author: UUID) = (countFragment ++ fr"where author = $author").query[Int].unique
  def getByAuthor(author: UUID) = (selectFragment ++ whereAnd(fr"author = $author")).query[Note].to[Seq]
  def getByAuthorSeq(authorSeq: NonEmptyList[UUID]) = (selectFragment ++ whereAnd(in(fr"author", authorSeq))).query[Note].to[Seq]
}

