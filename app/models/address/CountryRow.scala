/* Generated File */
package models.address

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object CountryRow {
  implicit val jsonEncoder: Encoder[CountryRow] = (r: CountryRow) => io.circe.Json.obj(
    ("countryId", r.countryId.asJson),
    ("country", r.country.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[CountryRow] = (c: io.circe.HCursor) => for {
    countryId <- c.downField("countryId").as[Int]
    country <- c.downField("country").as[String]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield CountryRow(countryId, country, lastUpdate)

  def empty(
    countryId: Int = 0,
    country: String = "",
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    CountryRow(countryId, country, lastUpdate)
  }
}

final case class CountryRow(
    countryId: Int,
    country: String,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("countryId", Some(countryId.toString)),
    DataField("country", Some(country)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "countryRow", pk = countryId.toString, entries = Map(
    "Country Id" -> Some(countryId.toString),
    "Country" -> Some(country),
    "Last Update" -> Some(lastUpdate.toString)
  ))
}
