/* Generated File */
package models.doobie.store

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.store.InventoryRow

object InventoryRowDoobie extends DoobieQueries[InventoryRow]("inventory") {
  override val countFragment = fr"""select count(*) from "inventory""""
  override val selectFragment = fr"""select "inventory_id", "film_id", "store_id", "last_update" from "inventory""""

  override val columns = Seq("inventory_id", "film_id", "store_id", "last_update")
  override val searchColumns = Seq("inventory_id", "film_id", "store_id")

  override def searchFragment(q: String) = {
    fr""""inventory_id"::text = $q or "film_id"::text = $q or "store_id"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(inventoryId: Long) = (selectFragment ++ whereAnd(fr"inventoryId = $inventoryId")).query[Option[InventoryRow]].unique
  def getByPrimaryKeySeq(inventoryIdSeq: NonEmptyList[Long]) = (selectFragment ++ in(fr"inventoryId", inventoryIdSeq)).query[InventoryRow].to[Seq]

  def countByFilmId(filmId: Int) = (countFragment ++ whereAnd(fr"filmId = $filmId")).query[Int].unique
  def getByFilmId(filmId: Int) = (selectFragment ++ whereAnd(fr"filmId = $filmId")).query[InventoryRow].to[Seq]
  def getByFilmIdSeq(filmIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"filmId", filmIdSeq))).query[InventoryRow].to[Seq]

  def countByStoreId(storeId: Int) = (countFragment ++ whereAnd(fr"storeId = $storeId")).query[Int].unique
  def getByStoreId(storeId: Int) = (selectFragment ++ whereAnd(fr"storeId = $storeId")).query[InventoryRow].to[Seq]
  def getByStoreIdSeq(storeIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"storeId", storeIdSeq))).query[InventoryRow].to[Seq]
}
