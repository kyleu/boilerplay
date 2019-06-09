/* Generated File */
package models.address

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object AddressRow {
  implicit val jsonEncoder: Encoder[AddressRow] = (r: AddressRow) => io.circe.Json.obj(
    ("addressId", r.addressId.asJson),
    ("address", r.address.asJson),
    ("address2", r.address2.asJson),
    ("district", r.district.asJson),
    ("cityId", r.cityId.asJson),
    ("postalCode", r.postalCode.asJson),
    ("phone", r.phone.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[AddressRow] = (c: io.circe.HCursor) => for {
    addressId <- c.downField("addressId").as[Int]
    address <- c.downField("address").as[String]
    address2 <- c.downField("address2").as[Option[String]]
    district <- c.downField("district").as[String]
    cityId <- c.downField("cityId").as[Int]
    postalCode <- c.downField("postalCode").as[Option[String]]
    phone <- c.downField("phone").as[String]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield AddressRow(addressId, address, address2, district, cityId, postalCode, phone, lastUpdate)

  def empty(
    addressId: Int = 0,
    address: String = "",
    address2: Option[String] = None,
    district: String = "",
    cityId: Int = 0,
    postalCode: Option[String] = None,
    phone: String = "",
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    AddressRow(addressId, address, address2, district, cityId, postalCode, phone, lastUpdate)
  }
}

final case class AddressRow(
    addressId: Int,
    address: String,
    address2: Option[String],
    district: String,
    cityId: Int,
    postalCode: Option[String],
    phone: String,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("addressId", Some(addressId.toString)),
    DataField("address", Some(address)),
    DataField("address2", address2),
    DataField("district", Some(district)),
    DataField("cityId", Some(cityId.toString)),
    DataField("postalCode", postalCode),
    DataField("phone", Some(phone)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "addressRow", pk = addressId.toString, title = s"address: $address, address2: ${address2.map(_.toString).getOrElse("∅")}, district: $district, cityId: $cityId, postalCode: ${postalCode.map(_.toString).getOrElse("∅")}, phone: $phone, lastUpdate: $lastUpdate")
}
