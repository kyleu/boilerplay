package models.queries.note

import com.kyleu.projectile.models.database.{Query, Row}
import models.note.NoteRow

object ModelNoteQueries {
  final case class GetByModel(model: String, pk: String) extends Query[Seq[NoteRow]] {
    override val name = "get.notes.by.model"
    override val sql = s"""select * from "${NoteRowQueries.tableName}" where "rel_type" = ? and "rel_pk" = ?"""
    override val values = Seq(model, pk)
    override def reduce(rows: Iterator[Row]) = rows.map(NoteRowQueries.fromRow).toList
  }

  final case class GetByModelSeq(models: Seq[(String, String)]) extends Query[Seq[NoteRow]] {
    override val name = "get.notes.by.model.seq"
    val clause = """("rel_type" = ? and "rel_pk" = ?)"""
    override val sql = s"""select * from "${NoteRowQueries.tableName}" where ${models.map(_ => clause).mkString(" or ")}"""
    override val values = models.flatMap(x => Seq(x._1, x._2))
    override def reduce(rows: Iterator[Row]) = rows.map(NoteRowQueries.fromRow).toList
  }
}
