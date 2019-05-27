/* Generated File */
package models.store

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object StoreRow {
  implicit val jsonEncoder: Encoder[StoreRow] = (r: StoreRow) => io.circe.Json.obj(
    ("storeId", r.storeId.asJson),
    ("managerStaffId", r.managerStaffId.asJson),
    ("addressId", r.addressId.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[StoreRow] = (c: io.circe.HCursor) => for {
    storeId <- c.downField("storeId").as[Int]
    managerStaffId <- c.downField("managerStaffId").as[Int]
    addressId <- c.downField("addressId").as[Int]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield StoreRow(storeId, managerStaffId, addressId, lastUpdate)

  def empty(
    storeId: Int = 0,
    managerStaffId: Int = 0,
    addressId: Int = 0,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    StoreRow(storeId, managerStaffId, addressId, lastUpdate)
  }
}

final case class StoreRow(
    storeId: Int,
    managerStaffId: Int,
    addressId: Int,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("storeId", Some(storeId.toString)),
    DataField("managerStaffId", Some(managerStaffId.toString)),
    DataField("addressId", Some(addressId.toString)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "storeRow", pk = storeId.toString, title = s"managerStaffId: $managerStaffId")
}
