package models.user

import com.mohiva.play.silhouette.api.util.Credentials
import play.api.data.Forms._
import play.api.data._

object UserForms {
  val signInForm = Form(mapping(
    "email" -> email,
    "password" -> nonEmptyText
  )(Credentials.apply)(Credentials.unapply))

  val registrationForm = Form(mapping(
    "username" -> nonEmptyText,
    "email" -> nonEmptyText,
    "password" -> nonEmptyText,
    "passwordConfirm" -> nonEmptyText
  )(RegistrationData.apply)(RegistrationData.unapply))

  val profileForm = Form(mapping(
    "username" -> nonEmptyText
  )(s => s)(s => Some(s)))

  final case class PasswordChange(oldPassword: String, newPassword: String, confirm: String)

  val changePasswordForm = Form(mapping(
    "old" -> nonEmptyText,
    "new" -> nonEmptyText,
    "confirm" -> nonEmptyText
  )(PasswordChange.apply)(PasswordChange.unapply))
}
