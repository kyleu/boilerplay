/* Generated File */
package models.queries.payment

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import models.payment.PaymentRow

object PaymentRowQueries extends BaseQueries[PaymentRow]("paymentRow", "payment") {
  override val fields = Seq(
    DatabaseField(title = "Payment Id", prop = "paymentId", col = "payment_id", typ = LongType),
    DatabaseField(title = "Customer Id", prop = "customerId", col = "customer_id", typ = IntegerType),
    DatabaseField(title = "Staff Id", prop = "staffId", col = "staff_id", typ = IntegerType),
    DatabaseField(title = "Rental Id", prop = "rentalId", col = "rental_id", typ = LongType),
    DatabaseField(title = "Amount", prop = "amount", col = "amount", typ = BigDecimalType),
    DatabaseField(title = "Payment Date", prop = "paymentDate", col = "payment_date", typ = TimestampZonedType)
  )
  override val pkColumns = Seq("payment_id")
  override protected val searchColumns = Seq("payment_id", "customer_id", "staff_id", "rental_id", "amount")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(paymentId: Long) = new GetByPrimaryKey(Seq(paymentId))
  def getByPrimaryKeySeq(paymentIdSeq: Seq[Long]) = new ColSeqQuery(column = "payment_id", values = paymentIdSeq)

  final case class CountByAmount(amount: BigDecimal) extends ColCount(column = "amount", values = Seq(amount))
  final case class GetByAmount(amount: BigDecimal, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("amount") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(amount)
  )
  final case class GetByAmountSeq(amountSeq: Seq[BigDecimal]) extends ColSeqQuery(column = "amount", values = amountSeq)

  final case class CountByCustomerId(customerId: Int) extends ColCount(column = "customer_id", values = Seq(customerId))
  final case class GetByCustomerId(customerId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("customer_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(customerId)
  )
  final case class GetByCustomerIdSeq(customerIdSeq: Seq[Int]) extends ColSeqQuery(column = "customer_id", values = customerIdSeq)

  final case class CountByPaymentId(paymentId: Long) extends ColCount(column = "payment_id", values = Seq(paymentId))
  final case class GetByPaymentId(paymentId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("payment_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(paymentId)
  )
  final case class GetByPaymentIdSeq(paymentIdSeq: Seq[Long]) extends ColSeqQuery(column = "payment_id", values = paymentIdSeq)

  final case class CountByRentalId(rentalId: Long) extends ColCount(column = "rental_id", values = Seq(rentalId))
  final case class GetByRentalId(rentalId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rental_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(rentalId)
  )
  final case class GetByRentalIdSeq(rentalIdSeq: Seq[Long]) extends ColSeqQuery(column = "rental_id", values = rentalIdSeq)

  final case class CountByStaffId(staffId: Int) extends ColCount(column = "staff_id", values = Seq(staffId))
  final case class GetByStaffId(staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("staff_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(staffId)
  )
  final case class GetByStaffIdSeq(staffIdSeq: Seq[Int]) extends ColSeqQuery(column = "staff_id", values = staffIdSeq)

  def insert(model: PaymentRow) = new Insert(model)
  def insertBatch(models: Seq[PaymentRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(paymentId: Long) = new RemoveByPrimaryKey(Seq[Any](paymentId))

  def update(paymentId: Long, fields: Seq[DataField]) = new UpdateFields(Seq[Any](paymentId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = PaymentRow(
    paymentId = LongType(row, "payment_id"),
    customerId = IntegerType(row, "customer_id"),
    staffId = IntegerType(row, "staff_id"),
    rentalId = LongType(row, "rental_id"),
    amount = BigDecimalType(row, "amount"),
    paymentDate = TimestampZonedType(row, "payment_date")
  )
}
