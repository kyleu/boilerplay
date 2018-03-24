package services.ui

import java.util.UUID

import models.ResponseMessage.UserSettings

object UserManager {
  var userId: Option[UUID] = None
  var username: Option[String] = None
  var email: Option[String] = None
  val rowsReturned = 100

  def onUserSettings(us: UserSettings) = {
    userId = Some(us.userId)
    username = Some(us.username)
    email = Some(us.email)
  }
}
