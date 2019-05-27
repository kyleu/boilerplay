/* Generated File */
package models.queries.store

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.store.StoreRow

object StoreRowQueries extends BaseQueries[StoreRow]("storeRow", "store") {
  override val fields = Seq(
    DatabaseField(title = "Store Id", prop = "storeId", col = "store_id", typ = IntegerType),
    DatabaseField(title = "Manager Staff Id", prop = "managerStaffId", col = "manager_staff_id", typ = IntegerType),
    DatabaseField(title = "Address Id", prop = "addressId", col = "address_id", typ = IntegerType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("store_id")
  override protected val searchColumns = Seq("store_id", "manager_staff_id", "address_id", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(storeId: Int) = new GetByPrimaryKey(Seq(storeId))
  def getByPrimaryKeySeq(storeIdSeq: Seq[Int]) = new ColSeqQuery(column = "store_id", values = storeIdSeq)

  final case class CountByAddressId(addressId: Int) extends ColCount(column = "address_id", values = Seq(addressId))
  final case class GetByAddressId(addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("address_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(addressId)
  )
  final case class GetByAddressIdSeq(addressIdSeq: Seq[Int]) extends ColSeqQuery(column = "address_id", values = addressIdSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  final case class CountByManagerStaffId(managerStaffId: Int) extends ColCount(column = "manager_staff_id", values = Seq(managerStaffId))
  final case class GetByManagerStaffId(managerStaffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("manager_staff_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(managerStaffId)
  )
  final case class GetByManagerStaffIdSeq(managerStaffIdSeq: Seq[Int]) extends ColSeqQuery(column = "manager_staff_id", values = managerStaffIdSeq)

  final case class CountByStoreId(storeId: Int) extends ColCount(column = "store_id", values = Seq(storeId))
  final case class GetByStoreId(storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("store_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(storeId)
  )
  final case class GetByStoreIdSeq(storeIdSeq: Seq[Int]) extends ColSeqQuery(column = "store_id", values = storeIdSeq)

  def insert(model: StoreRow) = new Insert(model)
  def insertBatch(models: Seq[StoreRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(storeId: Int) = new RemoveByPrimaryKey(Seq[Any](storeId))

  def update(storeId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](storeId), fields)

  override def fromRow(row: Row) = StoreRow(
    storeId = IntegerType(row, "store_id"),
    managerStaffId = IntegerType(row, "manager_staff_id"),
    addressId = IntegerType(row, "address_id"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
