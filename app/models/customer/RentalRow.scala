/* Generated File */
package models.customer

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object RentalRow {
  implicit val jsonEncoder: Encoder[RentalRow] = (r: RentalRow) => io.circe.Json.obj(
    ("rentalId", r.rentalId.asJson),
    ("rentalDate", r.rentalDate.asJson),
    ("inventoryId", r.inventoryId.asJson),
    ("customerId", r.customerId.asJson),
    ("returnDate", r.returnDate.asJson),
    ("staffId", r.staffId.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[RentalRow] = (c: io.circe.HCursor) => for {
    rentalId <- c.downField("rentalId").as[Long]
    rentalDate <- c.downField("rentalDate").as[ZonedDateTime]
    inventoryId <- c.downField("inventoryId").as[Long]
    customerId <- c.downField("customerId").as[Int]
    returnDate <- c.downField("returnDate").as[Option[ZonedDateTime]]
    staffId <- c.downField("staffId").as[Int]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield RentalRow(rentalId, rentalDate, inventoryId, customerId, returnDate, staffId, lastUpdate)

  def empty(
    rentalId: Long = 0L,
    rentalDate: ZonedDateTime = DateUtils.nowZoned,
    inventoryId: Long = 0L,
    customerId: Int = 0,
    returnDate: Option[ZonedDateTime] = None,
    staffId: Int = 0,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    RentalRow(rentalId, rentalDate, inventoryId, customerId, returnDate, staffId, lastUpdate)
  }
}

final case class RentalRow(
    rentalId: Long,
    rentalDate: ZonedDateTime,
    inventoryId: Long,
    customerId: Int,
    returnDate: Option[ZonedDateTime],
    staffId: Int,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("rentalId", Some(rentalId.toString)),
    DataField("rentalDate", Some(rentalDate.toString)),
    DataField("inventoryId", Some(inventoryId.toString)),
    DataField("customerId", Some(customerId.toString)),
    DataField("returnDate", returnDate.map(_.toString)),
    DataField("staffId", Some(staffId.toString)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "rentalRow", pk = rentalId.toString, title = s"rentalDate: $rentalDate, inventoryId: $inventoryId, customerId: $customerId")
}
