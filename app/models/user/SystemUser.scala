package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import java.time.LocalDateTime

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import models.result.data.{DataField, DataFieldModel}
import util.DateUtils

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
    profile = LoginInfo("anonymous", "system")
  )
}

case class SystemUser(
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

  def toSummary = {
    val title = username + " (" + id + ")"
    models.result.data.DataSummary(model = "user", pk = Seq(id.toString), title = title)
  }
}
