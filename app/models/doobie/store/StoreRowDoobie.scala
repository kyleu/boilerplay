/* Generated File */
package models.doobie.store

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.store.StoreRow

object StoreRowDoobie extends DoobieQueries[StoreRow]("store") {
  override val countFragment = fr"""select count(*) from "store""""
  override val selectFragment = fr"""select "store_id", "manager_staff_id", "address_id", "last_update" from "store""""

  override val columns = Seq("store_id", "manager_staff_id", "address_id", "last_update")
  override val searchColumns = Seq("store_id", "manager_staff_id", "address_id", "last_update")

  override def searchFragment(q: String) = {
    fr""""store_id"::text = $q or "manager_staff_id"::text = $q or "address_id"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(storeId: Int) = (selectFragment ++ whereAnd(fr"storeId = $storeId")).query[Option[StoreRow]].unique
  def getByPrimaryKeySeq(storeIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"storeId", storeIdSeq)).query[StoreRow].to[Seq]

  def countByAddressId(addressId: Int) = (countFragment ++ whereAnd(fr"addressId = $addressId")).query[Int].unique
  def getByAddressId(addressId: Int) = (selectFragment ++ whereAnd(fr"addressId = $addressId")).query[StoreRow].to[Seq]
  def getByAddressIdSeq(addressIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"addressId", addressIdSeq))).query[StoreRow].to[Seq]
}
