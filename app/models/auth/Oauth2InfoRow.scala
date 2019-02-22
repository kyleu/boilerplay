/* Generated File */
package models.auth

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import io.circe.Json
import java.time.LocalDateTime

object Oauth2InfoRow {
  implicit val jsonEncoder: Encoder[Oauth2InfoRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[Oauth2InfoRow] = deriveDecoder

  def empty(
    provider: String = "",
    key: String = "",
    accessToken: String = "",
    tokenType: Option[String] = None,
    expiresIn: Option[Long] = None,
    refreshToken: Option[String] = None,
    params: Option[Json] = None,
    created: LocalDateTime = DateUtils.now
  ) = {
    Oauth2InfoRow(provider, key, accessToken, tokenType, expiresIn, refreshToken, params, created)
  }
}

final case class Oauth2InfoRow(
    provider: String,
    key: String,
    accessToken: String,
    tokenType: Option[String],
    expiresIn: Option[Long],
    refreshToken: Option[String],
    params: Option[Json],
    created: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("provider", Some(provider)),
    DataField("key", Some(key)),
    DataField("accessToken", Some(accessToken)),
    DataField("tokenType", tokenType),
    DataField("expiresIn", expiresIn.map(_.toString)),
    DataField("refreshToken", refreshToken),
    DataField("params", params.map(_.toString)),
    DataField("created", Some(created.toString))
  )

  def toSummary = DataSummary(model = "oauth2InfoRow", pk = provider.toString + "/" + key.toString, title = s"provider: $provider, key: $key")
}
