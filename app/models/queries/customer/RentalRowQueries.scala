/* Generated File */
package models.queries.customer

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.customer.RentalRow

object RentalRowQueries extends BaseQueries[RentalRow]("rentalRow", "rental") {
  override val fields = Seq(
    DatabaseField(title = "Rental Id", prop = "rentalId", col = "rental_id", typ = LongType),
    DatabaseField(title = "Rental Date", prop = "rentalDate", col = "rental_date", typ = TimestampZonedType),
    DatabaseField(title = "Inventory Id", prop = "inventoryId", col = "inventory_id", typ = LongType),
    DatabaseField(title = "Customer Id", prop = "customerId", col = "customer_id", typ = IntegerType),
    DatabaseField(title = "Return Date", prop = "returnDate", col = "return_date", typ = TimestampZonedType),
    DatabaseField(title = "Staff Id", prop = "staffId", col = "staff_id", typ = IntegerType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("rental_id")
  override protected val searchColumns = Seq("rental_id", "rental_date", "inventory_id", "customer_id", "return_date", "staff_id")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(rentalId: Long) = new GetByPrimaryKey(Seq(rentalId))
  def getByPrimaryKeySeq(rentalIdSeq: Seq[Long]) = new ColSeqQuery(column = "rental_id", values = rentalIdSeq)

  final case class CountByCustomerId(customerId: Int) extends ColCount(column = "customer_id", values = Seq(customerId))
  final case class GetByCustomerId(customerId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("customer_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(customerId)
  )
  final case class GetByCustomerIdSeq(customerIdSeq: Seq[Int]) extends ColSeqQuery(column = "customer_id", values = customerIdSeq)

  final case class CountByInventoryId(inventoryId: Long) extends ColCount(column = "inventory_id", values = Seq(inventoryId))
  final case class GetByInventoryId(inventoryId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("inventory_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(inventoryId)
  )
  final case class GetByInventoryIdSeq(inventoryIdSeq: Seq[Long]) extends ColSeqQuery(column = "inventory_id", values = inventoryIdSeq)

  final case class CountByRentalDate(rentalDate: ZonedDateTime) extends ColCount(column = "rental_date", values = Seq(rentalDate))
  final case class GetByRentalDate(rentalDate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rental_date") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(rentalDate)
  )
  final case class GetByRentalDateSeq(rentalDateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "rental_date", values = rentalDateSeq)

  final case class CountByRentalId(rentalId: Long) extends ColCount(column = "rental_id", values = Seq(rentalId))
  final case class GetByRentalId(rentalId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rental_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(rentalId)
  )
  final case class GetByRentalIdSeq(rentalIdSeq: Seq[Long]) extends ColSeqQuery(column = "rental_id", values = rentalIdSeq)

  final case class CountByReturnDate(returnDate: ZonedDateTime) extends ColCount(column = "return_date", values = Seq(returnDate))
  final case class GetByReturnDate(returnDate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("return_date") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(returnDate)
  )
  final case class GetByReturnDateSeq(returnDateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "return_date", values = returnDateSeq)

  final case class CountByStaffId(staffId: Int) extends ColCount(column = "staff_id", values = Seq(staffId))
  final case class GetByStaffId(staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("staff_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(staffId)
  )
  final case class GetByStaffIdSeq(staffIdSeq: Seq[Int]) extends ColSeqQuery(column = "staff_id", values = staffIdSeq)

  def insert(model: RentalRow) = new Insert(model)
  def insertBatch(models: Seq[RentalRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(rentalId: Long) = new RemoveByPrimaryKey(Seq[Any](rentalId))

  def update(rentalId: Long, fields: Seq[DataField]) = new UpdateFields(Seq[Any](rentalId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = RentalRow(
    rentalId = LongType(row, "rental_id"),
    rentalDate = TimestampZonedType(row, "rental_date"),
    inventoryId = LongType(row, "inventory_id"),
    customerId = IntegerType(row, "customer_id"),
    returnDate = TimestampZonedType.opt(row, "return_date"),
    staffId = IntegerType(row, "staff_id"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
