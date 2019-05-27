/* Generated File */
package models.doobie.film

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.film.FilmCategoryRow

object FilmCategoryRowDoobie extends DoobieQueries[FilmCategoryRow]("film_category") {
  override val countFragment = fr"""select count(*) from "film_category""""
  override val selectFragment = fr"""select "film_id", "category_id", "last_update" from "film_category""""

  override val columns = Seq("film_id", "category_id", "last_update")
  override val searchColumns = Seq("film_id", "category_id", "last_update")

  override def searchFragment(q: String) = {
    fr""""film_id"::text = $q or "category_id"::text = $q or "last_update"::text = $q"""
  }

  // def getByPrimaryKey(filmId: Int, categoryId: Int) = ???

  def countByFilmId(filmId: Int) = (countFragment ++ whereAnd(fr"filmId = $filmId")).query[Int].unique
  def getByFilmId(filmId: Int) = (selectFragment ++ whereAnd(fr"filmId = $filmId")).query[FilmCategoryRow].to[Seq]
  def getByFilmIdSeq(filmIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"filmId", filmIdSeq))).query[FilmCategoryRow].to[Seq]

  def countByCategoryId(categoryId: Int) = (countFragment ++ whereAnd(fr"categoryId = $categoryId")).query[Int].unique
  def getByCategoryId(categoryId: Int) = (selectFragment ++ whereAnd(fr"categoryId = $categoryId")).query[FilmCategoryRow].to[Seq]
  def getByCategoryIdSeq(categoryIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"categoryId", categoryIdSeq))).query[FilmCategoryRow].to[Seq]
}
