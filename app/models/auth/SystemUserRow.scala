/* Generated File */
package models.auth

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID

object SystemUserRow {
  implicit val jsonEncoder: Encoder[SystemUserRow] = (r: SystemUserRow) => io.circe.Json.obj(
    ("id", r.id.asJson),
    ("username", r.username.asJson),
    ("provider", r.provider.asJson),
    ("key", r.key.asJson),
    ("role", r.role.asJson),
    ("created", r.created.asJson),
    ("settings", r.settings.asJson)
  )

  implicit val jsonDecoder: Decoder[SystemUserRow] = (c: io.circe.HCursor) => for {
    id <- c.downField("id").as[UUID]
    username <- c.downField("username").as[Option[String]]
    provider <- c.downField("provider").as[String]
    key <- c.downField("key").as[String]
    role <- c.downField("role").as[String]
    created <- c.downField("created").as[LocalDateTime]
    settings <- c.downField("settings").as[Json]
  } yield SystemUserRow(id, username, provider, key, role, created, settings)

  def empty(
    id: UUID = UUID.randomUUID,
    username: Option[String] = None,
    provider: String = "",
    key: String = "",
    role: String = "",
    created: LocalDateTime = DateUtils.now,
    settings: Json = Json.obj()
  ) = {
    SystemUserRow(id, username, provider, key, role, created, settings)
  }
}

final case class SystemUserRow(
    id: UUID,
    username: Option[String],
    provider: String,
    key: String,
    role: String,
    created: LocalDateTime,
    settings: Json
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("username", username),
    DataField("provider", Some(provider)),
    DataField("key", Some(key)),
    DataField("role", Some(role)),
    DataField("created", Some(created.toString)),
    DataField("settings", Some(settings.toString))
  )

  def toSummary = DataSummary(model = "systemUserRow", pk = id.toString, title = s"username: ${username.map(_.toString).getOrElse("∅")}, provider: $provider, key: $key")
}
