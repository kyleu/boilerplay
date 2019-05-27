/* Generated File */
package models.table.store

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.store.StoreRow
import models.table.address.AddressRowTable
import scala.language.higherKinds

object StoreRowTable {
  val query = TableQuery[StoreRowTable]

  def getByPrimaryKey(storeId: Int) = query.filter(_.storeId === storeId).result.headOption
  def getByPrimaryKeySeq(storeIdSeq: Seq[Int]) = query.filter(_.storeId.inSet(storeIdSeq)).result

  def getByAddressId(addressId: Int) = query.filter(_.addressId === addressId).result
  def getByAddressIdSeq(addressIdSeq: Seq[Int]) = query.filter(_.addressId.inSet(addressIdSeq)).result

  implicit class StoreRowTableExtensions[C[_]](q: Query[StoreRowTable, StoreRow, C]) {
    def withAddressRow = q.join(AddressRowTable.query).on(_.addressId === _.addressId)
    def withAddressRowOpt = q.joinLeft(AddressRowTable.query).on(_.addressId === _.addressId)
  }
}

class StoreRowTable(tag: slick.lifted.Tag) extends Table[StoreRow](tag, "store") {
  val storeId = column[Int]("store_id", O.PrimaryKey, O.AutoInc)
  val managerStaffId = column[Int]("manager_staff_id")
  val addressId = column[Int]("address_id")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (storeId, managerStaffId, addressId, lastUpdate) <> (
    (StoreRow.apply _).tupled,
    StoreRow.unapply
  )
}

