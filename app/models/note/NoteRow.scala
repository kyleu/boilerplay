/* Generated File */
package models.note

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime
import java.util.UUID

object NoteRow {
  implicit val jsonEncoder: Encoder[NoteRow] = (r: NoteRow) => io.circe.Json.obj(
    ("id", r.id.asJson),
    ("relType", r.relType.asJson),
    ("relPk", r.relPk.asJson),
    ("text", r.text.asJson),
    ("author", r.author.asJson),
    ("created", r.created.asJson)
  )

  implicit val jsonDecoder: Decoder[NoteRow] = (c: io.circe.HCursor) => for {
    id <- c.downField("id").as[UUID]
    relType <- c.downField("relType").as[Option[String]]
    relPk <- c.downField("relPk").as[Option[String]]
    text <- c.downField("text").as[String]
    author <- c.downField("author").as[UUID]
    created <- c.downField("created").as[LocalDateTime]
  } yield NoteRow(id, relType, relPk, text, author, created)

  def empty(
    id: UUID = UUID.randomUUID,
    relType: Option[String] = None,
    relPk: Option[String] = None,
    text: String = "",
    author: UUID = UUID.randomUUID,
    created: LocalDateTime = DateUtils.now
  ) = {
    NoteRow(id, relType, relPk, text, author, created)
  }
}

final case class NoteRow(
    id: UUID,
    relType: Option[String],
    relPk: Option[String],
    text: String,
    author: UUID,
    created: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("relType", relType),
    DataField("relPk", relPk),
    DataField("text", Some(text)),
    DataField("author", Some(author.toString)),
    DataField("created", Some(created.toString))
  )

  def toSummary = DataSummary(model = "noteRow", pk = id.toString, title = s"relType: ${relType.map(_.toString).getOrElse("∅")}, relPk: ${relPk.map(_.toString).getOrElse("∅")}, text: $text, author: $author, created: $created")
}
