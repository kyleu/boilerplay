package controllers

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Credentials, PasswordHasher}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.Application
import models.user.UserForms
import services.user.UserService
import util.web.ControllerUtils

import scala.concurrent.Future

@javax.inject.Singleton
class ProfileController @javax.inject.Inject() (
    override val app: Application,
    authInfoRepository: AuthInfoRepository,
    credentialsProvider: CredentialsProvider,
    hasher: PasswordHasher
) extends BaseController("profile") {
  import app.contexts.webContext

  def view = withSession("view") { implicit request => implicit td =>
    Future.successful(Ok(views.html.profile.view(request.identity)))
  }

  def save = withSession("view") { implicit request => implicit td =>
    UserForms.profileForm.bindFromRequest.fold(
      _ => Future.successful(BadRequest(views.html.profile.view(request.identity))),
      profileData => {
        val newPrefs = request.identity.preferences.copy(
          theme = profileData.theme
        )
        val newUser = request.identity.copy(username = profileData.username, preferences = newPrefs)
        app.userService.update(newUser)
        Future.successful(Redirect(controllers.routes.HomeController.home()))
      }
    )
  }

  def changePasswordForm = withSession("change-password-form") { implicit request => implicit td =>
    Future.successful(Ok(views.html.profile.changePassword(request.identity)))
  }

  def changePassword = withSession("change-password") { implicit request => implicit td =>
    def errorResponse(msg: String) = Redirect(controllers.routes.ProfileController.changePasswordForm()).flashing("error" -> msg)
    UserForms.changePasswordForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(errorResponse(ControllerUtils.errorsToString(formWithErrors.errors)))
      },
      changePass => {
        if (changePass.newPassword != changePass.confirm) {
          Future.successful(errorResponse("Passwords do not match."))
        } else {
          val email = request.identity.profile.providerKey
          credentialsProvider.authenticate(Credentials(email, changePass.oldPassword)).flatMap { loginInfo =>
            val okResponse = Redirect(controllers.routes.ProfileController.view()).flashing("success" -> "Password changed.")
            for {
              _ <- authInfoRepository.update(loginInfo, hasher.hash(changePass.newPassword))
              authenticator <- app.silhouette.env.authenticatorService.create(loginInfo)
              result <- app.silhouette.env.authenticatorService.renew(authenticator, okResponse)
            } yield result
          }.recover {
            case e: ProviderException => errorResponse(s"Old password does not match (${e.getMessage}).")
          }
        }
      }
    )
  }
}
