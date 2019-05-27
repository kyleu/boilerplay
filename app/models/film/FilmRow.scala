/* Generated File */
package models.film

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object FilmRow {
  implicit val jsonEncoder: Encoder[FilmRow] = (r: FilmRow) => io.circe.Json.obj(
    ("filmId", r.filmId.asJson),
    ("title", r.title.asJson),
    ("description", r.description.asJson),
    ("releaseYear", r.releaseYear.asJson),
    ("languageId", r.languageId.asJson),
    ("originalLanguageId", r.originalLanguageId.asJson),
    ("rentalDuration", r.rentalDuration.asJson),
    ("rentalRate", r.rentalRate.asJson),
    ("length", r.length.asJson),
    ("replacementCost", r.replacementCost.asJson),
    ("rating", r.rating.asJson),
    ("lastUpdate", r.lastUpdate.asJson),
    ("specialFeatures", r.specialFeatures.asJson),
    ("fulltext", r.fulltext.asJson)
  )

  implicit val jsonDecoder: Decoder[FilmRow] = (c: io.circe.HCursor) => for {
    filmId <- c.downField("filmId").as[Int]
    title <- c.downField("title").as[String]
    description <- c.downField("description").as[Option[String]]
    releaseYear <- c.downField("releaseYear").as[Option[Long]]
    languageId <- c.downField("languageId").as[Int]
    originalLanguageId <- c.downField("originalLanguageId").as[Option[Int]]
    rentalDuration <- c.downField("rentalDuration").as[Int]
    rentalRate <- c.downField("rentalRate").as[BigDecimal]
    length <- c.downField("length").as[Option[Int]]
    replacementCost <- c.downField("replacementCost").as[BigDecimal]
    rating <- c.downField("rating").as[Option[MpaaRatingType]]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
    specialFeatures <- c.downField("specialFeatures").as[Option[List[String]]]
    fulltext <- c.downField("fulltext").as[String]
  } yield FilmRow(filmId, title, description, releaseYear, languageId, originalLanguageId, rentalDuration, rentalRate, length, replacementCost, rating, lastUpdate, specialFeatures, fulltext)

  def empty(
    filmId: Int = 0,
    title: String = "",
    description: Option[String] = None,
    releaseYear: Option[Long] = None,
    languageId: Int = 0,
    originalLanguageId: Option[Int] = None,
    rentalDuration: Int = 0,
    rentalRate: BigDecimal = BigDecimal(0),
    length: Option[Int] = None,
    replacementCost: BigDecimal = BigDecimal(0),
    rating: Option[MpaaRatingType] = None,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned,
    specialFeatures: Option[List[String]] = None,
    fulltext: String = ""
  ) = {
    FilmRow(filmId, title, description, releaseYear, languageId, originalLanguageId, rentalDuration, rentalRate, length, replacementCost, rating, lastUpdate, specialFeatures, fulltext)
  }
}

final case class FilmRow(
    filmId: Int,
    title: String,
    description: Option[String],
    releaseYear: Option[Long],
    languageId: Int,
    originalLanguageId: Option[Int],
    rentalDuration: Int,
    rentalRate: BigDecimal,
    length: Option[Int],
    replacementCost: BigDecimal,
    rating: Option[MpaaRatingType],
    lastUpdate: ZonedDateTime,
    specialFeatures: Option[List[String]],
    fulltext: String
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("filmId", Some(filmId.toString)),
    DataField("title", Some(title)),
    DataField("description", description),
    DataField("releaseYear", releaseYear.map(_.toString)),
    DataField("languageId", Some(languageId.toString)),
    DataField("originalLanguageId", originalLanguageId.map(_.toString)),
    DataField("rentalDuration", Some(rentalDuration.toString)),
    DataField("rentalRate", Some(rentalRate.toString)),
    DataField("length", length.map(_.toString)),
    DataField("replacementCost", Some(replacementCost.toString)),
    DataField("rating", rating.map(_.value)),
    DataField("lastUpdate", Some(lastUpdate.toString)),
    DataField("specialFeatures", specialFeatures.map(v => "{ " + v.mkString(", ") + " }")),
    DataField("fulltext", Some(fulltext))
  )

  def toSummary = DataSummary(model = "filmRow", pk = filmId.toString, title = s"title: $title, languageId: $languageId, originalLanguageId: ${originalLanguageId.map(_.toString).getOrElse("∅")}, fulltext: $fulltext")
}
