/* Generated File */
package models.doobie.store

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.store.StaffRow

object StaffRowDoobie extends DoobieQueries[StaffRow]("staff") {
  override val countFragment = fr"""select count(*) from "staff""""
  override val selectFragment = fr"""select "staff_id", "first_name", "last_name", "address_id", "email", "store_id", "active", "username", "password", "last_update", "picture" from "staff""""

  override val columns = Seq("staff_id", "first_name", "last_name", "address_id", "email", "store_id", "active", "username", "password", "last_update", "picture")
  override val searchColumns = Seq("staff_id", "first_name", "last_name", "address_id", "email", "store_id", "username")

  override def searchFragment(q: String) = {
    fr""""staff_id"::text = $q or "first_name"::text = $q or "last_name"::text = $q or "address_id"::text = $q or "email"::text = $q or "store_id"::text = $q or "active"::text = $q or "username"::text = $q or "password"::text = $q or "last_update"::text = $q or "picture"::text = $q"""
  }

  def getByPrimaryKey(staffId: Int) = (selectFragment ++ whereAnd(fr"staffId = $staffId")).query[Option[StaffRow]].unique
  def getByPrimaryKeySeq(staffIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"staffId", staffIdSeq)).query[StaffRow].to[Seq]

  def countByStoreId(storeId: Int) = (countFragment ++ whereAnd(fr"storeId = $storeId")).query[Int].unique
  def getByStoreId(storeId: Int) = (selectFragment ++ whereAnd(fr"storeId = $storeId")).query[StaffRow].to[Seq]
  def getByStoreIdSeq(storeIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"storeId", storeIdSeq))).query[StaffRow].to[Seq]

  def countByAddressId(addressId: Int) = (countFragment ++ whereAnd(fr"addressId = $addressId")).query[Int].unique
  def getByAddressId(addressId: Int) = (selectFragment ++ whereAnd(fr"addressId = $addressId")).query[StaffRow].to[Seq]
  def getByAddressIdSeq(addressIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"addressId", addressIdSeq))).query[StaffRow].to[Seq]
}
