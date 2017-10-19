package models.note

import java.time.LocalDateTime
import java.util.UUID

import models.result.data.{DataField, DataFieldModel}

object Note {
  val empty = Note(
    id = UUID.randomUUID,
    relType = None,
    relPk = None,
    text = "",
    author = UUID.randomUUID,
    created = util.DateUtils.now
  )
}

case class Note(
    id: UUID = UUID.randomUUID(),
    relType: Option[String],
    relPk: Option[String],
    text: String,
    author: UUID,
    created: LocalDateTime = util.DateUtils.now
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("relType", relType),
    DataField("relPk", relPk),
    DataField("text", Some(text)),
    DataField("author", Some(author.toString)),
    DataField("created", Some(created.toString))
  )
}
