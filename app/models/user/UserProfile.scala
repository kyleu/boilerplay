package models.user

import java.util.UUID

import models.template.Theme
import java.time.LocalDateTime

import util.JsonSerializers._

object UserProfile {
  implicit val jsonEncoder: Encoder[UserProfile] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserProfile] = deriveDecoder

  def fromUser(u: SystemUser) = UserProfile(u.id, u.username, u.profile.providerKey, u.role, u.preferences.theme, u.created)
}

final case class UserProfile(
    id: UUID,
    username: String,
    email: String,
    role: Role,
    theme: Theme,
    created: LocalDateTime
)
