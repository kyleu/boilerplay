/* Generated File */
package models.doobie.customer

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.customer.RentalRow

object RentalRowDoobie extends DoobieQueries[RentalRow]("rental") {
  override val countFragment = fr"""select count(*) from "rental""""
  override val selectFragment = fr"""select "rental_id", "rental_date", "inventory_id", "customer_id", "return_date", "staff_id", "last_update" from "rental""""

  override val columns = Seq("rental_id", "rental_date", "inventory_id", "customer_id", "return_date", "staff_id", "last_update")
  override val searchColumns = Seq("rental_id", "rental_date", "inventory_id", "customer_id", "return_date", "staff_id")

  override def searchFragment(q: String) = {
    fr""""rental_id"::text = $q or "rental_date"::text = $q or "inventory_id"::text = $q or "customer_id"::text = $q or "return_date"::text = $q or "staff_id"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(rentalId: Long) = (selectFragment ++ whereAnd(fr"rentalId = $rentalId")).query[Option[RentalRow]].unique
  def getByPrimaryKeySeq(rentalIdSeq: NonEmptyList[Long]) = (selectFragment ++ in(fr"rentalId", rentalIdSeq)).query[RentalRow].to[Seq]

  def countByStaffId(staffId: Int) = (countFragment ++ whereAnd(fr"staffId = $staffId")).query[Int].unique
  def getByStaffId(staffId: Int) = (selectFragment ++ whereAnd(fr"staffId = $staffId")).query[RentalRow].to[Seq]
  def getByStaffIdSeq(staffIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"staffId", staffIdSeq))).query[RentalRow].to[Seq]

  def countByInventoryId(inventoryId: Long) = (countFragment ++ whereAnd(fr"inventoryId = $inventoryId")).query[Int].unique
  def getByInventoryId(inventoryId: Long) = (selectFragment ++ whereAnd(fr"inventoryId = $inventoryId")).query[RentalRow].to[Seq]
  def getByInventoryIdSeq(inventoryIdSeq: NonEmptyList[Long]) = (selectFragment ++ whereAnd(in(fr"inventoryId", inventoryIdSeq))).query[RentalRow].to[Seq]

  def countByCustomerId(customerId: Int) = (countFragment ++ whereAnd(fr"customerId = $customerId")).query[Int].unique
  def getByCustomerId(customerId: Int) = (selectFragment ++ whereAnd(fr"customerId = $customerId")).query[RentalRow].to[Seq]
  def getByCustomerIdSeq(customerIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"customerId", customerIdSeq))).query[RentalRow].to[Seq]
}
