package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import java.time.LocalDateTime
import util.DateUtils

object User {
  def empty() = User(
    id = UUID.randomUUID,
    username = "",
    preferences = UserPreferences.empty,
    profile = LoginInfo("anonymous", "guest")
  )
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
