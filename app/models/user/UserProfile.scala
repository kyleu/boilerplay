package models.user

import java.util.UUID
import java.time.LocalDateTime

import com.kyleu.projectile.models.user.{Role, SystemUser}
import com.kyleu.projectile.util.JsonSerializers._

object UserProfile {
  implicit val jsonEncoder: Encoder[UserProfile] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserProfile] = deriveDecoder

  def fromUser(u: SystemUser) = UserProfile(u.id, u.username, u.profile.providerKey, u.role, u.created)
}

final case class UserProfile(
    id: UUID,
    username: String,
    email: String,
    role: Role,
    created: LocalDateTime
)
