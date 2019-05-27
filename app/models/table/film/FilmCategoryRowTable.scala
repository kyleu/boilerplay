/* Generated File */
package models.table.film

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.film.FilmCategoryRow
import scala.language.higherKinds

object FilmCategoryRowTable {
  val query = TableQuery[FilmCategoryRowTable]

  def getByPrimaryKey(filmId: Int, categoryId: Int) = query.filter(o => o.filmId === filmId && o.categoryId === categoryId).result.headOption

  def getByFilmId(filmId: Int) = query.filter(_.filmId === filmId).result
  def getByFilmIdSeq(filmIdSeq: Seq[Int]) = query.filter(_.filmId.inSet(filmIdSeq)).result

  def getByCategoryId(categoryId: Int) = query.filter(_.categoryId === categoryId).result
  def getByCategoryIdSeq(categoryIdSeq: Seq[Int]) = query.filter(_.categoryId.inSet(categoryIdSeq)).result

  implicit class FilmCategoryRowTableExtensions[C[_]](q: Query[FilmCategoryRowTable, FilmCategoryRow, C]) {
    def withCategoryRow = q.join(CategoryRowTable.query).on(_.categoryId === _.categoryId)
    def withCategoryRowOpt = q.joinLeft(CategoryRowTable.query).on(_.categoryId === _.categoryId)
    def withFilmRow = q.join(FilmRowTable.query).on(_.filmId === _.filmId)
    def withFilmRowOpt = q.joinLeft(FilmRowTable.query).on(_.filmId === _.filmId)
  }
}

class FilmCategoryRowTable(tag: slick.lifted.Tag) extends Table[FilmCategoryRow](tag, "film_category") {
  val filmId = column[Int]("film_id")
  val categoryId = column[Int]("category_id")
  val lastUpdate = column[ZonedDateTime]("last_update")

  val modelPrimaryKey = primaryKey("pk_film_category", (filmId, categoryId))

  override val * = (filmId, categoryId, lastUpdate) <> (
    (FilmCategoryRow.apply _).tupled,
    FilmCategoryRow.unapply
  )
}

