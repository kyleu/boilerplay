package models.user

import io.circe.parser.decode
import io.circe.{Decoder, Encoder}
import models.template.Theme
import util.JsonSerializers._

object UserPreferences {
  implicit val jsonEncoder: Encoder[UserPreferences] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserPreferences] = deriveDecoder

  val empty = UserPreferences()

  def readFrom(s: String) = decode[UserPreferences](s) match {
    case Right(x) => x
    case Left(_) => UserPreferences.empty
  }
}

case class UserPreferences(
    theme: Theme = Theme.BlueGrey
)
