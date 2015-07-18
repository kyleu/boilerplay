package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import org.joda.time.LocalDateTime

case class User(
    id: UUID,
    username: Option[String],
    preferences: UserPreferences,
    profiles: Seq[LoginInfo],
    roles: Set[Role] = Set(Role.User),
    created: LocalDateTime
) extends Identity {
  def isGuest = profiles.isEmpty
  def isAdmin = roles.contains(models.user.Role.Admin)
}
