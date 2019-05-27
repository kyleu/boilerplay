/* Generated File */
package models.table.payment

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.payment.PaymentRow
import models.table.customer.{CustomerRowTable, RentalRowTable}
import models.table.store.StaffRowTable
import scala.language.higherKinds

object PaymentRowTable {
  val query = TableQuery[PaymentRowTable]

  def getByPrimaryKey(paymentId: Long) = query.filter(_.paymentId === paymentId).result.headOption
  def getByPrimaryKeySeq(paymentIdSeq: Seq[Long]) = query.filter(_.paymentId.inSet(paymentIdSeq)).result

  def getByStaffId(staffId: Int) = query.filter(_.staffId === staffId).result
  def getByStaffIdSeq(staffIdSeq: Seq[Int]) = query.filter(_.staffId.inSet(staffIdSeq)).result

  def getByRentalId(rentalId: Long) = query.filter(_.rentalId === rentalId).result
  def getByRentalIdSeq(rentalIdSeq: Seq[Long]) = query.filter(_.rentalId.inSet(rentalIdSeq)).result

  def getByCustomerId(customerId: Int) = query.filter(_.customerId === customerId).result
  def getByCustomerIdSeq(customerIdSeq: Seq[Int]) = query.filter(_.customerId.inSet(customerIdSeq)).result

  implicit class PaymentRowTableExtensions[C[_]](q: Query[PaymentRowTable, PaymentRow, C]) {
    def withStaffRow = q.join(StaffRowTable.query).on(_.staffId === _.staffId)
    def withStaffRowOpt = q.joinLeft(StaffRowTable.query).on(_.staffId === _.staffId)
    def withRentalRow = q.join(RentalRowTable.query).on(_.rentalId === _.rentalId)
    def withRentalRowOpt = q.joinLeft(RentalRowTable.query).on(_.rentalId === _.rentalId)
    def withCustomerRow = q.join(CustomerRowTable.query).on(_.customerId === _.customerId)
    def withCustomerRowOpt = q.joinLeft(CustomerRowTable.query).on(_.customerId === _.customerId)
  }
}

class PaymentRowTable(tag: slick.lifted.Tag) extends Table[PaymentRow](tag, "payment") {
  val paymentId = column[Long]("payment_id", O.PrimaryKey, O.AutoInc)
  val customerId = column[Int]("customer_id")
  val staffId = column[Int]("staff_id")
  val rentalId = column[Long]("rental_id")
  val amount = column[BigDecimal]("amount")
  val paymentDate = column[ZonedDateTime]("payment_date")

  override val * = (paymentId, customerId, staffId, rentalId, amount, paymentDate) <> (
    (PaymentRow.apply _).tupled,
    PaymentRow.unapply
  )
}

