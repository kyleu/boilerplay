/* Generated File */
package models.doobie.customer

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.customer.CustomerRow

object CustomerRowDoobie extends DoobieQueries[CustomerRow]("customer") {
  override val countFragment = fr"""select count(*) from "customer""""
  override val selectFragment = fr"""select "customer_id", "store_id", "first_name", "last_name", "email", "address_id", "activebool", "create_date", "last_update", "active" from "customer""""

  override val columns = Seq("customer_id", "store_id", "first_name", "last_name", "email", "address_id", "activebool", "create_date", "last_update", "active")
  override val searchColumns = Seq("customer_id", "store_id", "first_name", "last_name", "email", "address_id", "create_date")

  override def searchFragment(q: String) = {
    fr""""customer_id"::text = $q or "store_id"::text = $q or "first_name"::text = $q or "last_name"::text = $q or "email"::text = $q or "address_id"::text = $q or "activebool"::text = $q or "create_date"::text = $q or "last_update"::text = $q or "active"::text = $q"""
  }

  def getByPrimaryKey(customerId: Int) = (selectFragment ++ whereAnd(fr"customerId = $customerId")).query[Option[CustomerRow]].unique
  def getByPrimaryKeySeq(customerIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"customerId", customerIdSeq)).query[CustomerRow].to[Seq]

  def countByStoreId(storeId: Int) = (countFragment ++ whereAnd(fr"storeId = $storeId")).query[Int].unique
  def getByStoreId(storeId: Int) = (selectFragment ++ whereAnd(fr"storeId = $storeId")).query[CustomerRow].to[Seq]
  def getByStoreIdSeq(storeIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"storeId", storeIdSeq))).query[CustomerRow].to[Seq]

  def countByAddressId(addressId: Int) = (countFragment ++ whereAnd(fr"addressId = $addressId")).query[Int].unique
  def getByAddressId(addressId: Int) = (selectFragment ++ whereAnd(fr"addressId = $addressId")).query[CustomerRow].to[Seq]
  def getByAddressIdSeq(addressIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"addressId", addressIdSeq))).query[CustomerRow].to[Seq]
}
