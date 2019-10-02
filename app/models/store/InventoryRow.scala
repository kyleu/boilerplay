/* Generated File */
package models.store

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object InventoryRow {
  implicit val jsonEncoder: Encoder[InventoryRow] = (r: InventoryRow) => io.circe.Json.obj(
    ("inventoryId", r.inventoryId.asJson),
    ("filmId", r.filmId.asJson),
    ("storeId", r.storeId.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[InventoryRow] = (c: io.circe.HCursor) => for {
    inventoryId <- c.downField("inventoryId").as[Long]
    filmId <- c.downField("filmId").as[Int]
    storeId <- c.downField("storeId").as[Int]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield InventoryRow(inventoryId, filmId, storeId, lastUpdate)

  def empty(
    inventoryId: Long = 0L,
    filmId: Int = 0,
    storeId: Int = 0,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    InventoryRow(inventoryId, filmId, storeId, lastUpdate)
  }
}

final case class InventoryRow(
    inventoryId: Long,
    filmId: Int,
    storeId: Int,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("inventoryId", Some(inventoryId.toString)),
    DataField("filmId", Some(filmId.toString)),
    DataField("storeId", Some(storeId.toString)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "inventoryRow", pk = inventoryId.toString, entries = Map(
    "Inventory Id" -> Some(inventoryId.toString),
    "Film Id" -> Some(filmId.toString),
    "Store Id" -> Some(storeId.toString)
  ))
}
