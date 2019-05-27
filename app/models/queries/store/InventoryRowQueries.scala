/* Generated File */
package models.queries.store

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.store.InventoryRow

object InventoryRowQueries extends BaseQueries[InventoryRow]("inventoryRow", "inventory") {
  override val fields = Seq(
    DatabaseField(title = "Inventory Id", prop = "inventoryId", col = "inventory_id", typ = LongType),
    DatabaseField(title = "Film Id", prop = "filmId", col = "film_id", typ = IntegerType),
    DatabaseField(title = "Store Id", prop = "storeId", col = "store_id", typ = IntegerType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("inventory_id")
  override protected val searchColumns = Seq("inventory_id", "film_id", "store_id")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(inventoryId: Long) = new GetByPrimaryKey(Seq(inventoryId))
  def getByPrimaryKeySeq(inventoryIdSeq: Seq[Long]) = new ColSeqQuery(column = "inventory_id", values = inventoryIdSeq)

  final case class CountByFilmId(filmId: Int) extends ColCount(column = "film_id", values = Seq(filmId))
  final case class GetByFilmId(filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("film_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(filmId)
  )
  final case class GetByFilmIdSeq(filmIdSeq: Seq[Int]) extends ColSeqQuery(column = "film_id", values = filmIdSeq)

  final case class CountByInventoryId(inventoryId: Long) extends ColCount(column = "inventory_id", values = Seq(inventoryId))
  final case class GetByInventoryId(inventoryId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("inventory_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(inventoryId)
  )
  final case class GetByInventoryIdSeq(inventoryIdSeq: Seq[Long]) extends ColSeqQuery(column = "inventory_id", values = inventoryIdSeq)

  final case class CountByStoreId(storeId: Int) extends ColCount(column = "store_id", values = Seq(storeId))
  final case class GetByStoreId(storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("store_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(storeId)
  )
  final case class GetByStoreIdSeq(storeIdSeq: Seq[Int]) extends ColSeqQuery(column = "store_id", values = storeIdSeq)

  def insert(model: InventoryRow) = new Insert(model)
  def insertBatch(models: Seq[InventoryRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(inventoryId: Long) = new RemoveByPrimaryKey(Seq[Any](inventoryId))

  def update(inventoryId: Long, fields: Seq[DataField]) = new UpdateFields(Seq[Any](inventoryId), fields)

  override def fromRow(row: Row) = InventoryRow(
    inventoryId = LongType(row, "inventory_id"),
    filmId = IntegerType(row, "film_id"),
    storeId = IntegerType(row, "store_id"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
