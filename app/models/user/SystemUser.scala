package models.user

import java.time.LocalDateTime
import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import models.result.data.{DataField, DataFieldModel}
import util.DateUtils
import util.JsonSerializers._

object SystemUser {
  private[this] implicit val jsonLoginInfoEncoder: Encoder[LoginInfo] = deriveEncoder
  private[this] implicit val jsonLoginInfoDecoder: Decoder[LoginInfo] = deriveDecoder

  implicit val jsonEncoder: Encoder[SystemUser] = deriveEncoder
  implicit val jsonDecoder: Decoder[SystemUser] = deriveDecoder

  def empty() = SystemUser(
    id = UUID.randomUUID,
    username = "",
    preferences = UserPreferences.empty,
    profile = LoginInfo("anonymous", "guest")
  )

  val system = SystemUser(
    id = UUID.fromString("88888888-8888-8888-8888-888888888888"),
    username = "",
    preferences = UserPreferences.empty,
    profile = LoginInfo("anonymous", "system"),
    role = Role.Admin
  )

  val guest = SystemUser(
    id = UUID.fromString("77777777-7777-7777-7777-777777777777"),
    username = "guest",
    preferences = UserPreferences.empty,
    profile = LoginInfo("anonymous", "guest")
  )

  val api = SystemUser(
    id = UUID.fromString("44444444-4444-4444-4444-444444444444"),
    username = "api",
    preferences = UserPreferences.empty,
    profile = LoginInfo("anonymous", "api"),
    role = Role.Admin
  )
}

final case class SystemUser(
    id: UUID,
    username: String,
    preferences: UserPreferences,
    profile: LoginInfo,
    role: Role = Role.User,
    created: LocalDateTime = DateUtils.now
) extends Identity with DataFieldModel {
  val email = profile.providerID
  val prefs = {
    preferences.asJson.spaces2
  }

  def isAdmin = role == Role.Admin

  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("username", Some(username)),
    DataField("preferences", Some(preferences.toString)),
    DataField("provider", Some(profile.providerID)),
    DataField("key", Some(profile.providerKey)),
    DataField("role", Some(role.toString)),
    DataField("created", Some(created.toString))
  )

  def toSummary = {
    val title = username + " (" + id + ")"
    models.result.data.DataSummary(model = "systemUser", pk = Seq(id.toString), title = title)
  }
}
