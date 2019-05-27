/* Generated File */
package models.customer

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object LanguageRow {
  implicit val jsonEncoder: Encoder[LanguageRow] = (r: LanguageRow) => io.circe.Json.obj(
    ("languageId", r.languageId.asJson),
    ("name", r.name.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[LanguageRow] = (c: io.circe.HCursor) => for {
    languageId <- c.downField("languageId").as[Int]
    name <- c.downField("name").as[String]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield LanguageRow(languageId, name, lastUpdate)

  def empty(
    languageId: Int = 0,
    name: String = "",
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    LanguageRow(languageId, name, lastUpdate)
  }
}

final case class LanguageRow(
    languageId: Int,
    name: String,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("languageId", Some(languageId.toString)),
    DataField("name", Some(name)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "languageRow", pk = languageId.toString, title = s"languageId: $languageId")
}
