/* Generated File */
package models.table.address

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.address.AddressRow
import scala.language.higherKinds

object AddressRowTable {
  val query = TableQuery[AddressRowTable]

  def getByPrimaryKey(addressId: Int) = query.filter(_.addressId === addressId).result.headOption
  def getByPrimaryKeySeq(addressIdSeq: Seq[Int]) = query.filter(_.addressId.inSet(addressIdSeq)).result

  def getByCityId(cityId: Int) = query.filter(_.cityId === cityId).result
  def getByCityIdSeq(cityIdSeq: Seq[Int]) = query.filter(_.cityId.inSet(cityIdSeq)).result

  implicit class AddressRowTableExtensions[C[_]](q: Query[AddressRowTable, AddressRow, C]) {
    def withCityRow = q.join(CityRowTable.query).on(_.cityId === _.cityId)
    def withCityRowOpt = q.joinLeft(CityRowTable.query).on(_.cityId === _.cityId)
  }
}

class AddressRowTable(tag: slick.lifted.Tag) extends Table[AddressRow](tag, "address") {
  val addressId = column[Int]("address_id", O.PrimaryKey, O.AutoInc)
  val address = column[String]("address")
  val address2 = column[Option[String]]("address2")
  val district = column[String]("district")
  val cityId = column[Int]("city_id")
  val postalCode = column[Option[String]]("postal_code")
  val phone = column[String]("phone")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (addressId, address, address2, district, cityId, postalCode, phone, lastUpdate) <> (
    (AddressRow.apply _).tupled,
    AddressRow.unapply
  )
}

