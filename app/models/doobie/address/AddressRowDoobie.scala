/* Generated File */
package models.doobie.address

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.address.AddressRow

object AddressRowDoobie extends DoobieQueries[AddressRow]("address") {
  override val countFragment = fr"""select count(*) from "address""""
  override val selectFragment = fr"""select "address_id", "address", "address2", "district", "city_id", "postal_code", "phone", "last_update" from "address""""

  override val columns = Seq("address_id", "address", "address2", "district", "city_id", "postal_code", "phone", "last_update")
  override val searchColumns = Seq("address_id", "address", "address2", "district", "city_id", "postal_code", "phone", "last_update")

  override def searchFragment(q: String) = {
    fr""""address_id"::text = $q or "address"::text = $q or "address2"::text = $q or "district"::text = $q or "city_id"::text = $q or "postal_code"::text = $q or "phone"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(addressId: Int) = (selectFragment ++ whereAnd(fr"addressId = $addressId")).query[Option[AddressRow]].unique
  def getByPrimaryKeySeq(addressIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"addressId", addressIdSeq)).query[AddressRow].to[Seq]

  def countByCityId(cityId: Int) = (countFragment ++ whereAnd(fr"cityId = $cityId")).query[Int].unique
  def getByCityId(cityId: Int) = (selectFragment ++ whereAnd(fr"cityId = $cityId")).query[AddressRow].to[Seq]
  def getByCityIdSeq(cityIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"cityId", cityIdSeq))).query[AddressRow].to[Seq]
}
