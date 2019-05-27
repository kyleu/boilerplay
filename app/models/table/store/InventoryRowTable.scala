/* Generated File */
package models.table.store

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.store.InventoryRow
import models.table.film.FilmRowTable
import scala.language.higherKinds

object InventoryRowTable {
  val query = TableQuery[InventoryRowTable]

  def getByPrimaryKey(inventoryId: Long) = query.filter(_.inventoryId === inventoryId).result.headOption
  def getByPrimaryKeySeq(inventoryIdSeq: Seq[Long]) = query.filter(_.inventoryId.inSet(inventoryIdSeq)).result

  def getByFilmId(filmId: Int) = query.filter(_.filmId === filmId).result
  def getByFilmIdSeq(filmIdSeq: Seq[Int]) = query.filter(_.filmId.inSet(filmIdSeq)).result

  def getByStoreId(storeId: Int) = query.filter(_.storeId === storeId).result
  def getByStoreIdSeq(storeIdSeq: Seq[Int]) = query.filter(_.storeId.inSet(storeIdSeq)).result

  implicit class InventoryRowTableExtensions[C[_]](q: Query[InventoryRowTable, InventoryRow, C]) {
    def withStoreRow = q.join(StoreRowTable.query).on(_.storeId === _.storeId)
    def withStoreRowOpt = q.joinLeft(StoreRowTable.query).on(_.storeId === _.storeId)
    def withFilmRow = q.join(FilmRowTable.query).on(_.filmId === _.filmId)
    def withFilmRowOpt = q.joinLeft(FilmRowTable.query).on(_.filmId === _.filmId)
  }
}

class InventoryRowTable(tag: slick.lifted.Tag) extends Table[InventoryRow](tag, "inventory") {
  val inventoryId = column[Long]("inventory_id", O.PrimaryKey, O.AutoInc)
  val filmId = column[Int]("film_id")
  val storeId = column[Int]("store_id")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (inventoryId, filmId, storeId, lastUpdate) <> (
    (InventoryRow.apply _).tupled,
    InventoryRow.unapply
  )
}

