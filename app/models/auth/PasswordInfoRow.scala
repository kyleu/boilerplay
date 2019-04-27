/* Generated File */
package models.auth

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

object PasswordInfoRow {
  implicit val jsonEncoder: Encoder[PasswordInfoRow] = (r: PasswordInfoRow) => io.circe.Json.obj(
    ("provider", r.provider.asJson),
    ("key", r.key.asJson),
    ("hasher", r.hasher.asJson),
    ("password", r.password.asJson),
    ("salt", r.salt.asJson),
    ("created", r.created.asJson)
  )

  implicit val jsonDecoder: Decoder[PasswordInfoRow] = (c: io.circe.HCursor) => for {
    provider <- c.downField("provider").as[String]
    key <- c.downField("key").as[String]
    hasher <- c.downField("hasher").as[String]
    password <- c.downField("password").as[String]
    salt <- c.downField("salt").as[Option[String]]
    created <- c.downField("created").as[LocalDateTime]
  } yield PasswordInfoRow(provider, key, hasher, password, salt, created)

  def empty(
    provider: String = "",
    key: String = "",
    hasher: String = "",
    password: String = "",
    salt: Option[String] = None,
    created: LocalDateTime = DateUtils.now
  ) = {
    PasswordInfoRow(provider, key, hasher, password, salt, created)
  }
}

final case class PasswordInfoRow(
    provider: String,
    key: String,
    hasher: String,
    password: String,
    salt: Option[String],
    created: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("provider", Some(provider)),
    DataField("key", Some(key)),
    DataField("hasher", Some(hasher)),
    DataField("password", Some(password)),
    DataField("salt", salt),
    DataField("created", Some(created.toString))
  )

  def toSummary = DataSummary(model = "passwordInfoRow", pk = provider.toString + "/" + key.toString, title = s"provider: $provider, key: $key")
}
