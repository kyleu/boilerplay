package models.note

import java.time.LocalDateTime
import java.util.UUID
import models.result.data.{DataField, DataFieldModel}

object Note {
  val empty = Note(
    id = UUID.randomUUID,
    relType = Some(""),
    relPk = Some(""),
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
    DataField("relType", relType.map(_.toString)),
    DataField("relPk", relPk.map(_.toString)),
    DataField("text", Some(text.toString)),
    DataField("author", Some(author.toString)),
    DataField("created", Some(created.toString))
  )
}
