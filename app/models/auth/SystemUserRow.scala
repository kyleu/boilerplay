/* Generated File */
package models.auth

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime
import java.util.UUID

object SystemUserRow {
  implicit val jsonEncoder: Encoder[SystemUserRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[SystemUserRow] = deriveDecoder

  def empty(
    id: UUID = UUID.randomUUID,
    username: Option[String] = None,
    provider: String = "",
    key: String = "",
    role: String = "",
    created: LocalDateTime = DateUtils.now
  ) = {
    SystemUserRow(id, username, provider, key, role, created)
  }
}

final case class SystemUserRow(
    id: UUID,
    username: Option[String],
    provider: String,
    key: String,
    role: String,
    created: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("username", username),
    DataField("provider", Some(provider)),
    DataField("key", Some(key)),
    DataField("role", Some(role)),
    DataField("created", Some(created.toString))
  )

  def toSummary = DataSummary(model = "systemUserRow", pk = id.toString, title = s"username: ${username.map(_.toString).getOrElse("∅")}, provider: $provider, key: $key")
}
