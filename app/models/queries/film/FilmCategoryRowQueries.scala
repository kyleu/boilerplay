/* Generated File */
package models.queries.film

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.film.FilmCategoryRow

object FilmCategoryRowQueries extends BaseQueries[FilmCategoryRow]("filmCategoryRow", "film_category") {
  override val fields = Seq(
    DatabaseField(title = "Film Id", prop = "filmId", col = "film_id", typ = IntegerType),
    DatabaseField(title = "Category Id", prop = "categoryId", col = "category_id", typ = IntegerType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("film_id", "category_id")
  override protected val searchColumns = Seq("film_id", "category_id", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(filmId: Int, categoryId: Int) = new GetByPrimaryKey(Seq[Any](filmId, categoryId))
  def getByPrimaryKeySeq(idSeq: Seq[(Int, Int)]) = new SeqQuery(
    whereClause = Some(idSeq.map(_ => "(\"film_id\" = ? and \"category_id\" = ?)").mkString(" or ")),
    values = idSeq.flatMap(_.productIterator.toSeq)
  )

  final case class CountByCategoryId(categoryId: Int) extends ColCount(column = "category_id", values = Seq(categoryId))
  final case class GetByCategoryId(categoryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("category_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(categoryId)
  )
  final case class GetByCategoryIdSeq(categoryIdSeq: Seq[Int]) extends ColSeqQuery(column = "category_id", values = categoryIdSeq)

  final case class CountByFilmId(filmId: Int) extends ColCount(column = "film_id", values = Seq(filmId))
  final case class GetByFilmId(filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("film_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(filmId)
  )
  final case class GetByFilmIdSeq(filmIdSeq: Seq[Int]) extends ColSeqQuery(column = "film_id", values = filmIdSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  def insert(model: FilmCategoryRow) = new Insert(model)
  def insertBatch(models: Seq[FilmCategoryRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(filmId: Int, categoryId: Int) = new RemoveByPrimaryKey(Seq[Any](filmId, categoryId))

  def update(filmId: Int, categoryId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](filmId, categoryId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = FilmCategoryRow(
    filmId = IntegerType(row, "film_id"),
    categoryId = IntegerType(row, "category_id"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
