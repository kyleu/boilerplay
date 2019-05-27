/* Generated File */
package models.table.customer

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.customer.RentalRow
import models.table.store.{InventoryRowTable, StaffRowTable}
import scala.language.higherKinds

object RentalRowTable {
  val query = TableQuery[RentalRowTable]

  def getByPrimaryKey(rentalId: Long) = query.filter(_.rentalId === rentalId).result.headOption
  def getByPrimaryKeySeq(rentalIdSeq: Seq[Long]) = query.filter(_.rentalId.inSet(rentalIdSeq)).result

  def getByStaffId(staffId: Int) = query.filter(_.staffId === staffId).result
  def getByStaffIdSeq(staffIdSeq: Seq[Int]) = query.filter(_.staffId.inSet(staffIdSeq)).result

  def getByInventoryId(inventoryId: Long) = query.filter(_.inventoryId === inventoryId).result
  def getByInventoryIdSeq(inventoryIdSeq: Seq[Long]) = query.filter(_.inventoryId.inSet(inventoryIdSeq)).result

  def getByCustomerId(customerId: Int) = query.filter(_.customerId === customerId).result
  def getByCustomerIdSeq(customerIdSeq: Seq[Int]) = query.filter(_.customerId.inSet(customerIdSeq)).result

  implicit class RentalRowTableExtensions[C[_]](q: Query[RentalRowTable, RentalRow, C]) {
    def withStaffRow = q.join(StaffRowTable.query).on(_.staffId === _.staffId)
    def withStaffRowOpt = q.joinLeft(StaffRowTable.query).on(_.staffId === _.staffId)
    def withInventoryRow = q.join(InventoryRowTable.query).on(_.inventoryId === _.inventoryId)
    def withInventoryRowOpt = q.joinLeft(InventoryRowTable.query).on(_.inventoryId === _.inventoryId)
    def withCustomerRow = q.join(CustomerRowTable.query).on(_.customerId === _.customerId)
    def withCustomerRowOpt = q.joinLeft(CustomerRowTable.query).on(_.customerId === _.customerId)
  }
}

class RentalRowTable(tag: slick.lifted.Tag) extends Table[RentalRow](tag, "rental") {
  val rentalId = column[Long]("rental_id", O.PrimaryKey, O.AutoInc)
  val rentalDate = column[ZonedDateTime]("rental_date")
  val inventoryId = column[Long]("inventory_id")
  val customerId = column[Int]("customer_id")
  val returnDate = column[Option[ZonedDateTime]]("return_date")
  val staffId = column[Int]("staff_id")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (rentalId, rentalDate, inventoryId, customerId, returnDate, staffId, lastUpdate) <> (
    (RentalRow.apply _).tupled,
    RentalRow.unapply
  )
}

