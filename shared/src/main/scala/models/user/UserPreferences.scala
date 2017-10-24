package models.user

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import models.template.Theme

object UserPreferences {
  implicit val jsonEncoder: Encoder[UserPreferences] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserPreferences] = deriveDecoder

  val empty = UserPreferences()
}

case class UserPreferences(
    theme: Theme = Theme.BlueGrey
)
