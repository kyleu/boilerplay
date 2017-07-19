package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import java.time.LocalDateTime
import util.DateUtils

object User {
  val mock = User(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Test User", UserPreferences.empty, LoginInfo("anonymous", "guest"))
}

case class User(
    id: UUID,
    username: String,
    preferences: UserPreferences,
    profile: LoginInfo,
    role: Role = Role.User,
    created: LocalDateTime = DateUtils.now
) extends Identity {
  def isAdmin = role == Role.Admin
}
