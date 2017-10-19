package models.note

import java.time.LocalDateTime
import java.util.UUID

import models.result.data.{DataField, DataFieldModel}

object Note {
  val empty = Note(
    id = UUID.randomUUID,
    relType = Some(""),
    relPk = Nil,
    text = "",
    author = UUID.randomUUID,
    created = util.DateUtils.now
  )
}

case class Note(
    id: UUID,
    relType: Option[String],
    relPk: Seq[String],
    text: String,
    author: UUID,
    created: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("relType", relType.map(_.toString)),
    DataField("relPk", Some(relPk.mkString(", "))),
    DataField("text", Some(text.toString)),
    DataField("author", Some(author.toString)),
    DataField("created", Some(created.toString))
  )
}
