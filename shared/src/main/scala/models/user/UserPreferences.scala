package models.user

import io.circe.{Decoder, Encoder}
import models.template.Theme
import util.JsonSerializers.Circe._

object UserPreferences {
  implicit val jsonEncoder: Encoder[UserPreferences] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserPreferences] = deriveDecoder

  val empty = UserPreferences()
}

case class UserPreferences(
    theme: Theme = Theme.BlueGrey
)
