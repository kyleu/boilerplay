/* Generated File */
package models.doobie.note

import cats.data.NonEmptyList
import java.util.UUID
import models.note.NoteRow
import services.database.doobie.DoobieQueries
import services.database.doobie.DoobieQueryService.Imports._

object NoteRowDoobie extends DoobieQueries[NoteRow]("note") {
  override val countFragment = fr"""select count(*) from "note""""
  override val selectFragment = fr"""select "id", "rel_type", "rel_pk", "text", "author", "created" from "note""""

  override val columns = Seq("id", "rel_type", "rel_pk", "text", "author", "created")
  override val searchColumns = Seq("id", "rel_type", "rel_pk", "text", "author", "created")

  override def searchFragment(q: String) = {
    fr""""id"::text = $q or "rel_type"::text = $q or "rel_pk"::text = $q or "text"::text = $q or "author"::text = $q or "created"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[NoteRow]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[NoteRow].to[Seq]

  def countByAuthor(author: UUID) = (countFragment ++ whereAnd(fr"author = $author")).query[Int].unique
  def getByAuthor(author: UUID) = (selectFragment ++ whereAnd(fr"author = $author")).query[NoteRow].to[Seq]
  def getByAuthorSeq(authorSeq: NonEmptyList[UUID]) = (selectFragment ++ whereAnd(in(fr"author", authorSeq))).query[NoteRow].to[Seq]
}
