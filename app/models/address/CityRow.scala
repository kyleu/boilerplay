/* Generated File */
package models.address

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object CityRow {
  implicit val jsonEncoder: Encoder[CityRow] = (r: CityRow) => io.circe.Json.obj(
    ("cityId", r.cityId.asJson),
    ("city", r.city.asJson),
    ("countryId", r.countryId.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[CityRow] = (c: io.circe.HCursor) => for {
    cityId <- c.downField("cityId").as[Int]
    city <- c.downField("city").as[String]
    countryId <- c.downField("countryId").as[Int]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield CityRow(cityId, city, countryId, lastUpdate)

  def empty(
    cityId: Int = 0,
    city: String = "",
    countryId: Int = 0,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    CityRow(cityId, city, countryId, lastUpdate)
  }
}

final case class CityRow(
    cityId: Int,
    city: String,
    countryId: Int,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("cityId", Some(cityId.toString)),
    DataField("city", Some(city)),
    DataField("countryId", Some(countryId.toString)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "cityRow", pk = cityId.toString, title = s"countryId: $countryId")
}
