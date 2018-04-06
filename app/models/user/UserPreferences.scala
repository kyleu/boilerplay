package models.user

import models.template.Theme
import util.JsonSerializers._

object UserPreferences {
  implicit val jsonEncoder: Encoder[UserPreferences] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserPreferences] = deriveDecoder

  val empty = UserPreferences()

  def readFrom(s: String) = decodeJson[UserPreferences](s) match {
    case Right(x) => x
    case Left(_) => UserPreferences.empty
  }
}

final case class UserPreferences(
    theme: Theme = Theme.BlueGrey
)
