package models.user

import java.time.LocalDateTime
import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import util.{DateUtils, JsonSerializers}

object RichUser {
  def apply(u: User): RichUser = RichUser(
    id = u.id,
    username = u.username,
    preferences = JsonSerializers.readPreferences(u.prefs),
    profile = LoginInfo("credentials", u.email),
    role = Role.withName(u.role),
    created = u.created
  )
}

case class RichUser(
    id: UUID,
    username: String,
    preferences: UserPreferences,
    profile: LoginInfo,
    role: Role = Role.User,
    created: LocalDateTime = DateUtils.now
) extends Identity {
  def isAdmin = role == Role.Admin

  lazy val toUser = User(
    id = id,
    username = username,
    prefs = JsonSerializers.writePreferences(preferences),
    email = profile.providerKey,
    role = role.toString,
    created = created
  )
}
