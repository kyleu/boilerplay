/* Generated File */
package models.store

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object StaffRow {
  implicit val jsonEncoder: Encoder[StaffRow] = (r: StaffRow) => io.circe.Json.obj(
    ("staffId", r.staffId.asJson),
    ("firstName", r.firstName.asJson),
    ("lastName", r.lastName.asJson),
    ("addressId", r.addressId.asJson),
    ("email", r.email.asJson),
    ("storeId", r.storeId.asJson),
    ("active", r.active.asJson),
    ("username", r.username.asJson),
    ("password", r.password.asJson),
    ("lastUpdate", r.lastUpdate.asJson),
    ("picture", r.picture.asJson)
  )

  implicit val jsonDecoder: Decoder[StaffRow] = (c: io.circe.HCursor) => for {
    staffId <- c.downField("staffId").as[Int]
    firstName <- c.downField("firstName").as[String]
    lastName <- c.downField("lastName").as[String]
    addressId <- c.downField("addressId").as[Int]
    email <- c.downField("email").as[Option[String]]
    storeId <- c.downField("storeId").as[Int]
    active <- c.downField("active").as[Boolean]
    username <- c.downField("username").as[String]
    password <- c.downField("password").as[Option[String]]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
    picture <- c.downField("picture").as[Option[Array[Byte]]]
  } yield StaffRow(staffId, firstName, lastName, addressId, email, storeId, active, username, password, lastUpdate, picture)

  def empty(
    staffId: Int = 0,
    firstName: String = "",
    lastName: String = "",
    addressId: Int = 0,
    email: Option[String] = None,
    storeId: Int = 0,
    active: Boolean = false,
    username: String = "",
    password: Option[String] = None,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned,
    picture: Option[Array[Byte]] = None
  ) = {
    StaffRow(staffId, firstName, lastName, addressId, email, storeId, active, username, password, lastUpdate, picture)
  }
}

final case class StaffRow(
    staffId: Int,
    firstName: String,
    lastName: String,
    addressId: Int,
    email: Option[String],
    storeId: Int,
    active: Boolean,
    username: String,
    password: Option[String],
    lastUpdate: ZonedDateTime,
    picture: Option[Array[Byte]]
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("staffId", Some(staffId.toString)),
    DataField("firstName", Some(firstName)),
    DataField("lastName", Some(lastName)),
    DataField("addressId", Some(addressId.toString)),
    DataField("email", email),
    DataField("storeId", Some(storeId.toString)),
    DataField("active", Some(active.toString)),
    DataField("username", Some(username)),
    DataField("password", password),
    DataField("lastUpdate", Some(lastUpdate.toString)),
    DataField("picture", picture.map(_.toString))
  )

  def toSummary = DataSummary(model = "staffRow", pk = staffId.toString, title = s"firstName: $firstName, lastName: $lastName, addressId: $addressId, email: ${email.map(_.toString).getOrElse("∅")}, storeId: $storeId, username: $username")
}
