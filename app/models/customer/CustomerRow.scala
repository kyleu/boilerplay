/* Generated File */
package models.customer

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.{LocalDate, ZonedDateTime}

object CustomerRow {
  implicit val jsonEncoder: Encoder[CustomerRow] = (r: CustomerRow) => io.circe.Json.obj(
    ("customerId", r.customerId.asJson),
    ("storeId", r.storeId.asJson),
    ("firstName", r.firstName.asJson),
    ("lastName", r.lastName.asJson),
    ("email", r.email.asJson),
    ("addressId", r.addressId.asJson),
    ("activebool", r.activebool.asJson),
    ("createDate", r.createDate.asJson),
    ("lastUpdate", r.lastUpdate.asJson),
    ("active", r.active.asJson)
  )

  implicit val jsonDecoder: Decoder[CustomerRow] = (c: io.circe.HCursor) => for {
    customerId <- c.downField("customerId").as[Int]
    storeId <- c.downField("storeId").as[Int]
    firstName <- c.downField("firstName").as[String]
    lastName <- c.downField("lastName").as[String]
    email <- c.downField("email").as[Option[String]]
    addressId <- c.downField("addressId").as[Int]
    activebool <- c.downField("activebool").as[Boolean]
    createDate <- c.downField("createDate").as[LocalDate]
    lastUpdate <- c.downField("lastUpdate").as[Option[ZonedDateTime]]
    active <- c.downField("active").as[Option[Long]]
  } yield CustomerRow(customerId, storeId, firstName, lastName, email, addressId, activebool, createDate, lastUpdate, active)

  def empty(
    customerId: Int = 0,
    storeId: Int = 0,
    firstName: String = "",
    lastName: String = "",
    email: Option[String] = None,
    addressId: Int = 0,
    activebool: Boolean = true,
    createDate: LocalDate = DateUtils.today,
    lastUpdate: Option[ZonedDateTime] = None,
    active: Option[Long] = None
  ) = {
    CustomerRow(customerId, storeId, firstName, lastName, email, addressId, activebool, createDate, lastUpdate, active)
  }
}

final case class CustomerRow(
    customerId: Int,
    storeId: Int,
    firstName: String,
    lastName: String,
    email: Option[String],
    addressId: Int,
    activebool: Boolean,
    createDate: LocalDate,
    lastUpdate: Option[ZonedDateTime],
    active: Option[Long]
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("customerId", Some(customerId.toString)),
    DataField("storeId", Some(storeId.toString)),
    DataField("firstName", Some(firstName)),
    DataField("lastName", Some(lastName)),
    DataField("email", email),
    DataField("addressId", Some(addressId.toString)),
    DataField("activebool", Some(activebool.toString)),
    DataField("createDate", Some(createDate.toString)),
    DataField("lastUpdate", lastUpdate.map(_.toString)),
    DataField("active", active.map(_.toString))
  )

  def toSummary = DataSummary(model = "customerRow", pk = customerId.toString, title = s"storeId: $storeId, firstName: $firstName, lastName: $lastName, email: ${email.map(_.toString).getOrElse("∅")}, addressId: $addressId")
}
