/* Generated File */
package models.table.store

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.store.StaffRow
import models.table.address.AddressRowTable
import scala.language.higherKinds

object StaffRowTable {
  val query = TableQuery[StaffRowTable]

  def getByPrimaryKey(staffId: Int) = query.filter(_.staffId === staffId).result.headOption
  def getByPrimaryKeySeq(staffIdSeq: Seq[Int]) = query.filter(_.staffId.inSet(staffIdSeq)).result

  def getByStoreId(storeId: Int) = query.filter(_.storeId === storeId).result
  def getByStoreIdSeq(storeIdSeq: Seq[Int]) = query.filter(_.storeId.inSet(storeIdSeq)).result

  def getByAddressId(addressId: Int) = query.filter(_.addressId === addressId).result
  def getByAddressIdSeq(addressIdSeq: Seq[Int]) = query.filter(_.addressId.inSet(addressIdSeq)).result

  implicit class StaffRowTableExtensions[C[_]](q: Query[StaffRowTable, StaffRow, C]) {
    def withAddressRow = q.join(AddressRowTable.query).on(_.addressId === _.addressId)
    def withAddressRowOpt = q.joinLeft(AddressRowTable.query).on(_.addressId === _.addressId)
    def withStoreRow = q.join(StoreRowTable.query).on(_.storeId === _.storeId)
    def withStoreRowOpt = q.joinLeft(StoreRowTable.query).on(_.storeId === _.storeId)
  }
}

class StaffRowTable(tag: slick.lifted.Tag) extends Table[StaffRow](tag, "staff") {
  val staffId = column[Int]("staff_id", O.PrimaryKey, O.AutoInc)
  val firstName = column[String]("first_name")
  val lastName = column[String]("last_name")
  val addressId = column[Int]("address_id")
  val email = column[Option[String]]("email")
  val storeId = column[Int]("store_id")
  val active = column[Boolean]("active")
  val username = column[String]("username")
  val password = column[Option[String]]("password")
  val lastUpdate = column[ZonedDateTime]("last_update")
  val picture = column[Option[Array[Byte]]]("picture")

  override val * = (staffId, firstName, lastName, addressId, email, storeId, active, username, password, lastUpdate, picture) <> (
    (StaffRow.apply _).tupled,
    StaffRow.unapply
  )
}

