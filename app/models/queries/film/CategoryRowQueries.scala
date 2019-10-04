/* Generated File */
package models.queries.film

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.film.CategoryRow

object CategoryRowQueries extends BaseQueries[CategoryRow]("categoryRow", "category") {
  override val fields = Seq(
    DatabaseField(title = "Category Id", prop = "categoryId", col = "category_id", typ = IntegerType),
    DatabaseField(title = "Name", prop = "name", col = "name", typ = StringType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override val pkColumns = Seq("category_id")
  override protected val searchColumns = Seq("category_id", "name", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(categoryId: Int) = new GetByPrimaryKey(Seq(categoryId))
  def getByPrimaryKeySeq(categoryIdSeq: Seq[Int]) = new ColSeqQuery(column = "category_id", values = categoryIdSeq)

  final case class CountByCategoryId(categoryId: Int) extends ColCount(column = "category_id", values = Seq(categoryId))
  final case class GetByCategoryId(categoryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("category_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(categoryId)
  )
  final case class GetByCategoryIdSeq(categoryIdSeq: Seq[Int]) extends ColSeqQuery(column = "category_id", values = categoryIdSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  final case class CountByName(nameArg: String) extends ColCount(column = "name", values = Seq(nameArg))
  final case class GetByName(nameArg: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("name") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(nameArg)
  )
  final case class GetByNameSeq(nameArgSeq: Seq[String]) extends ColSeqQuery(column = "name", values = nameArgSeq)

  def insert(model: CategoryRow) = new Insert(model)
  def insertBatch(models: Seq[CategoryRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(categoryId: Int) = new RemoveByPrimaryKey(Seq[Any](categoryId))

  def update(categoryId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](categoryId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = CategoryRow(
    categoryId = IntegerType(row, "category_id"),
    name = StringType(row, "name"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
