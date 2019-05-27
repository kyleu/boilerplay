/* Generated File */
package models.table.customer

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.{LocalDate, ZonedDateTime}
import models.customer.CustomerRow
import models.table.address.AddressRowTable
import models.table.store.StoreRowTable
import scala.language.higherKinds

object CustomerRowTable {
  val query = TableQuery[CustomerRowTable]

  def getByPrimaryKey(customerId: Int) = query.filter(_.customerId === customerId).result.headOption
  def getByPrimaryKeySeq(customerIdSeq: Seq[Int]) = query.filter(_.customerId.inSet(customerIdSeq)).result

  def getByStoreId(storeId: Int) = query.filter(_.storeId === storeId).result
  def getByStoreIdSeq(storeIdSeq: Seq[Int]) = query.filter(_.storeId.inSet(storeIdSeq)).result

  def getByAddressId(addressId: Int) = query.filter(_.addressId === addressId).result
  def getByAddressIdSeq(addressIdSeq: Seq[Int]) = query.filter(_.addressId.inSet(addressIdSeq)).result

  implicit class CustomerRowTableExtensions[C[_]](q: Query[CustomerRowTable, CustomerRow, C]) {
    def withAddressRow = q.join(AddressRowTable.query).on(_.addressId === _.addressId)
    def withAddressRowOpt = q.joinLeft(AddressRowTable.query).on(_.addressId === _.addressId)
    def withStoreRow = q.join(StoreRowTable.query).on(_.storeId === _.storeId)
    def withStoreRowOpt = q.joinLeft(StoreRowTable.query).on(_.storeId === _.storeId)
  }
}

class CustomerRowTable(tag: slick.lifted.Tag) extends Table[CustomerRow](tag, "customer") {
  val customerId = column[Int]("customer_id", O.PrimaryKey, O.AutoInc)
  val storeId = column[Int]("store_id")
  val firstName = column[String]("first_name")
  val lastName = column[String]("last_name")
  val email = column[Option[String]]("email")
  val addressId = column[Int]("address_id")
  val activebool = column[Boolean]("activebool")
  val createDate = column[LocalDate]("create_date")
  val lastUpdate = column[Option[ZonedDateTime]]("last_update")
  val active = column[Option[Long]]("active")

  override val * = (customerId, storeId, firstName, lastName, email, addressId, activebool, createDate, lastUpdate, active) <> (
    (CustomerRow.apply _).tupled,
    CustomerRow.unapply
  )
}

