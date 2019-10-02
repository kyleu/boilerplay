/* Generated File */
package models.film

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object ActorRow {
  implicit val jsonEncoder: Encoder[ActorRow] = (r: ActorRow) => io.circe.Json.obj(
    ("actorId", r.actorId.asJson),
    ("firstName", r.firstName.asJson),
    ("lastName", r.lastName.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[ActorRow] = (c: io.circe.HCursor) => for {
    actorId <- c.downField("actorId").as[Int]
    firstName <- c.downField("firstName").as[String]
    lastName <- c.downField("lastName").as[String]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield ActorRow(actorId, firstName, lastName, lastUpdate)

  def empty(
    actorId: Int = 0,
    firstName: String = "",
    lastName: String = "",
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    ActorRow(actorId, firstName, lastName, lastUpdate)
  }
}

final case class ActorRow(
    actorId: Int,
    firstName: String,
    lastName: String,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("actorId", Some(actorId.toString)),
    DataField("firstName", Some(firstName)),
    DataField("lastName", Some(lastName)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "actorRow", pk = actorId.toString, entries = Map(
    "Actor Id" -> Some(actorId.toString),
    "First Name" -> Some(firstName),
    "Last Name" -> Some(lastName),
    "Last Update" -> Some(lastUpdate.toString)
  ))
}
