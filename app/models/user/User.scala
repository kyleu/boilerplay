package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import java.time.LocalDateTime

import models.result.data.{DataField, DataFieldModel}
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
) extends Identity with DataFieldModel {
  def isAdmin = role == Role.Admin

  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("username", Some(username)),
    DataField("preferences", Some(preferences.toString)),
    DataField("profile", Some(profile.toString)),
    DataField("role", Some(role.toString)),
    DataField("created", Some(created.toString))
  )
}
