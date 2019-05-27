/* Generated File */
package models.table.film

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.film.{FilmRow, MpaaRatingType}
import models.table.customer.LanguageRowTable
import models.table.film.MpaaRatingTypeColumnType.mpaaRatingTypeColumnType
import scala.language.higherKinds

object FilmRowTable {
  val query = TableQuery[FilmRowTable]

  def getByPrimaryKey(filmId: Int) = query.filter(_.filmId === filmId).result.headOption
  def getByPrimaryKeySeq(filmIdSeq: Seq[Int]) = query.filter(_.filmId.inSet(filmIdSeq)).result

  def getByLanguageId(languageId: Int) = query.filter(_.languageId === languageId).result
  def getByLanguageIdSeq(languageIdSeq: Seq[Int]) = query.filter(_.languageId.inSet(languageIdSeq)).result

  def getByOriginalLanguageId(originalLanguageId: Int) = query.filter(_.originalLanguageId === originalLanguageId).result
  def getByOriginalLanguageIdSeq(originalLanguageIdSeq: Seq[Int]) = query.filter(_.originalLanguageId.inSet(originalLanguageIdSeq)).result

  implicit class FilmRowTableExtensions[C[_]](q: Query[FilmRowTable, FilmRow, C]) {
    def withLanguageRowByLanguageId = q.join(LanguageRowTable.query).on(_.languageId === _.languageId)
    def withLanguageRowByLanguageIdOpt = q.joinLeft(LanguageRowTable.query).on(_.languageId === _.languageId)
    def withLanguageRowByOriginalLanguageId = q.join(LanguageRowTable.query).on(_.originalLanguageId === _.languageId)
    def withLanguageRowByOriginalLanguageIdOpt = q.joinLeft(LanguageRowTable.query).on(_.originalLanguageId === _.languageId)
  }
}

class FilmRowTable(tag: slick.lifted.Tag) extends Table[FilmRow](tag, "film") {
  val filmId = column[Int]("film_id", O.PrimaryKey, O.AutoInc)
  val title = column[String]("title")
  val description = column[Option[String]]("description")
  val releaseYear = column[Option[Long]]("release_year")
  val languageId = column[Int]("language_id")
  val originalLanguageId = column[Option[Int]]("original_language_id")
  val rentalDuration = column[Int]("rental_duration")
  val rentalRate = column[BigDecimal]("rental_rate")
  val length = column[Option[Int]]("length")
  val replacementCost = column[BigDecimal]("replacement_cost")
  val rating = column[Option[MpaaRatingType]]("rating")
  val lastUpdate = column[ZonedDateTime]("last_update")
  val specialFeatures = column[Option[List[String]]]("special_features")
  val fulltext = column[String]("fulltext")

  override val * = (filmId, title, description, releaseYear, languageId, originalLanguageId, rentalDuration, rentalRate, length, replacementCost, rating, lastUpdate, specialFeatures, fulltext) <> (
    (FilmRow.apply _).tupled,
    FilmRow.unapply
  )
}

