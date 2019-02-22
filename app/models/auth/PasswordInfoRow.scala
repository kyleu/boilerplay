/* Generated File */
package models.auth

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

object PasswordInfoRow {
  implicit val jsonEncoder: Encoder[PasswordInfoRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[PasswordInfoRow] = deriveDecoder

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
