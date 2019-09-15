/* Generated File */
package models.doobie.film

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.doobie.film.MpaaRatingTypeDoobie.mpaaRatingTypeMeta
import models.film.FilmRow

object FilmRowDoobie extends DoobieQueries[FilmRow]("film") {
  override val countFragment = fr"""select count(*) from "film""""
  override val selectFragment = fr"""select "film_id", "title", "description", "release_year", "language_id", "original_language_id", "rental_duration", "rental_rate", "length", "replacement_cost", "rating", "last_update", "special_features", "fulltext" from "film""""

  override val columns = Seq("film_id", "title", "description", "release_year", "language_id", "original_language_id", "rental_duration", "rental_rate", "length", "replacement_cost", "rating", "last_update", "special_features", "fulltext")
  override val searchColumns = Seq("film_id", "title", "description", "release_year", "language_id", "original_language_id", "rental_duration", "rental_rate", "length", "rating", "last_update", "fulltext")

  override def searchFragment(q: String) = {
    fr""""film_id"::text = $q or "title"::text = $q or "description"::text = $q or "release_year"::text = $q or "language_id"::text = $q or "original_language_id"::text = $q or "rental_duration"::text = $q or "rental_rate"::text = $q or "length"::text = $q or "replacement_cost"::text = $q or "rating"::text = $q or "last_update"::text = $q or "special_features"::text = $q or "fulltext"::text = $q"""
  }

  def getByPrimaryKey(filmId: Int) = (selectFragment ++ whereAnd(fr"filmId = $filmId")).query[Option[FilmRow]].unique
  def getByPrimaryKeySeq(filmIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"filmId", filmIdSeq)).query[FilmRow].to[Seq]

  def countByLanguageId(languageId: Int) = (countFragment ++ whereAnd(fr"languageId = $languageId")).query[Int].unique
  def getByLanguageId(languageId: Int) = (selectFragment ++ whereAnd(fr"languageId = $languageId")).query[FilmRow].to[Seq]
  def getByLanguageIdSeq(languageIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"languageId", languageIdSeq))).query[FilmRow].to[Seq]

  def countByOriginalLanguageId(originalLanguageId: Int) = (countFragment ++ whereAnd(fr"originalLanguageId = $originalLanguageId")).query[Int].unique
  def getByOriginalLanguageId(originalLanguageId: Int) = (selectFragment ++ whereAnd(fr"originalLanguageId = $originalLanguageId")).query[FilmRow].to[Seq]
  def getByOriginalLanguageIdSeq(originalLanguageIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"originalLanguageId", originalLanguageIdSeq))).query[FilmRow].to[Seq]
}
