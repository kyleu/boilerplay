/* Generated File */
package models.film

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object FilmCategoryRow {
  implicit val jsonEncoder: Encoder[FilmCategoryRow] = (r: FilmCategoryRow) => io.circe.Json.obj(
    ("filmId", r.filmId.asJson),
    ("categoryId", r.categoryId.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[FilmCategoryRow] = (c: io.circe.HCursor) => for {
    filmId <- c.downField("filmId").as[Int]
    categoryId <- c.downField("categoryId").as[Int]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield FilmCategoryRow(filmId, categoryId, lastUpdate)

  def empty(
    filmId: Int = 0,
    categoryId: Int = 0,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    FilmCategoryRow(filmId, categoryId, lastUpdate)
  }
}

final case class FilmCategoryRow(
    filmId: Int,
    categoryId: Int,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("filmId", Some(filmId.toString)),
    DataField("categoryId", Some(categoryId.toString)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "filmCategoryRow", pk = filmId.toString + "/" + categoryId.toString, title = s"lastUpdate: $lastUpdate")
}
