package models.queries.note

import models.database.{Query, Row}
import models.note.Note

object ModelNoteQueries {
  case class GetByModel(model: String, pk: String) extends Query[Seq[Note]] {
    override val name = "get.notes.by.model"
    override val sql = s"""select * from "${NoteQueries.tableName}" where "rel_type" = ? and "rel_pk" = ?"""
    override val values = Seq(model, pk)
    override def reduce(rows: Iterator[Row]) = rows.map(NoteQueries.fromRow).toList
  }
}
