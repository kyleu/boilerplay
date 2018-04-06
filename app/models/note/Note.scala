package models.note

import java.time.LocalDateTime
import java.util.UUID

import models.result.data.{DataField, DataFieldModel, DataSummary}
import util.JsonSerializers._

object Note {
  implicit val jsonEncoder: Encoder[Note] = deriveEncoder
  implicit val jsonDecoder: Decoder[Note] = deriveDecoder

  def empty(author: Option[UUID] = None) = Note(
    id = UUID.randomUUID,
    relType = Some(""),
    relPk = Some(""),
    text = "",
    author = author.getOrElse(UUID.randomUUID),
    created = util.DateUtils.now
  )
}

final case class Note(
    id: UUID = UUID.randomUUID(),
    relType: Option[String] = None,
    relPk: Option[String] = None,
    text: String = "???",
    author: UUID = UUID.randomUUID,
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

  def toSummary = DataSummary(model = "note", pk = Seq(id.toString), title = s"$relType / $relPk / $text ($id)")
}
