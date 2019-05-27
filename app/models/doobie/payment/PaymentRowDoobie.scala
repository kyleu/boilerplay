/* Generated File */
package models.doobie.payment

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.payment.PaymentRow

object PaymentRowDoobie extends DoobieQueries[PaymentRow]("payment") {
  override val countFragment = fr"""select count(*) from "payment""""
  override val selectFragment = fr"""select "payment_id", "customer_id", "staff_id", "rental_id", "amount", "payment_date" from "payment""""

  override val columns = Seq("payment_id", "customer_id", "staff_id", "rental_id", "amount", "payment_date")
  override val searchColumns = Seq("payment_id", "customer_id", "staff_id", "rental_id", "amount")

  override def searchFragment(q: String) = {
    fr""""payment_id"::text = $q or "customer_id"::text = $q or "staff_id"::text = $q or "rental_id"::text = $q or "amount"::text = $q or "payment_date"::text = $q"""
  }

  def getByPrimaryKey(paymentId: Long) = (selectFragment ++ whereAnd(fr"paymentId = $paymentId")).query[Option[PaymentRow]].unique
  def getByPrimaryKeySeq(paymentIdSeq: NonEmptyList[Long]) = (selectFragment ++ in(fr"paymentId", paymentIdSeq)).query[PaymentRow].to[Seq]

  def countByStaffId(staffId: Int) = (countFragment ++ whereAnd(fr"staffId = $staffId")).query[Int].unique
  def getByStaffId(staffId: Int) = (selectFragment ++ whereAnd(fr"staffId = $staffId")).query[PaymentRow].to[Seq]
  def getByStaffIdSeq(staffIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"staffId", staffIdSeq))).query[PaymentRow].to[Seq]

  def countByRentalId(rentalId: Long) = (countFragment ++ whereAnd(fr"rentalId = $rentalId")).query[Int].unique
  def getByRentalId(rentalId: Long) = (selectFragment ++ whereAnd(fr"rentalId = $rentalId")).query[PaymentRow].to[Seq]
  def getByRentalIdSeq(rentalIdSeq: NonEmptyList[Long]) = (selectFragment ++ whereAnd(in(fr"rentalId", rentalIdSeq))).query[PaymentRow].to[Seq]

  def countByCustomerId(customerId: Int) = (countFragment ++ whereAnd(fr"customerId = $customerId")).query[Int].unique
  def getByCustomerId(customerId: Int) = (selectFragment ++ whereAnd(fr"customerId = $customerId")).query[PaymentRow].to[Seq]
  def getByCustomerIdSeq(customerIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"customerId", customerIdSeq))).query[PaymentRow].to[Seq]
}
