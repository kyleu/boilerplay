package controllers.auth

import java.util.UUID

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.user.{Role, SystemUser}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider

import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.auth.UserCredentials
import models.settings.SettingKeyType
import models.user._
import services.settings.SettingsService
import services.user.{SystemUserSearchService, SystemUserService}

import scala.concurrent.Future

@javax.inject.Singleton
class RegistrationController @javax.inject.Inject() (
    override val app: Application,
    userSearchService: SystemUserSearchService,
    authInfoRepository: AuthInfoRepository,
    hasher: PasswordHasher,
    userService: SystemUserService,
    settings: SettingsService
) extends AuthController("registration") {
  def registrationForm(email: Option[String] = None) = withoutSession("form") { implicit request => implicit td =>
    if (settings.allowRegistration) {
      val form = UserForms.registrationForm.fill(RegistrationData(
        username = email.map(e => if (e.contains('@')) { e.substring(0, e.indexOf('@')) } else { "" }).getOrElse(""),
        email = email.getOrElse("")
      ))
      Future.successful(Ok(views.html.auth.register(request.identity, form)))
    } else {
      Future.successful(Redirect(controllers.routes.HomeController.home()).flashing("error" -> "You cannot sign up at this time. Contact your administrator."))
    }
  }

  def register = withoutSession("register") { implicit request => implicit td =>
    if (!settings.allowRegistration) {
      throw new IllegalStateException("You cannot sign up at this time. Contact your administrator.")
    }
    UserForms.registrationForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.register(request.identity, form))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email.toLowerCase)
        userSearchService.getByLoginInfo(loginInfo).flatMap {
          case _ if data.password != data.passwordConfirm => Future.successful(
            Redirect(controllers.auth.routes.RegistrationController.register()).flashing("error" -> "Passwords do not match.")
          )
          case Some(_) => Future.successful(
            Redirect(controllers.auth.routes.RegistrationController.register()).flashing("error" -> "That email address is already in use.")
          )
          case None =>
            val authInfo = hasher.hash(data.password)
            val role = Role.withValue(settings(SettingKeyType.DefaultNewUserRole))
            val user = SystemUser(
              id = UUID.randomUUID,
              username = data.username,
              profile = loginInfo,
              role = role
            )
            val creds = UserCredentials(user, request.remoteAddress)
            userService.insert(creds, user).flatMap { userSaved =>
              val result = request.session.get("returnUrl") match {
                case Some(url) => Redirect(url).withSession(request.session - "returnUrl")
                case None => Redirect(controllers.routes.HomeController.home())
              }
              for {
                _ <- authInfoRepository.add(loginInfo, authInfo)
                authenticator <- app.silhouette.env.authenticatorService.create(loginInfo)
                value <- app.silhouette.env.authenticatorService.init(authenticator)
                result <- app.silhouette.env.authenticatorService.embed(value, result)
              } yield {
                app.silhouette.env.eventBus.publish(SignUpEvent(userSaved, request))
                app.silhouette.env.eventBus.publish(LoginEvent(userSaved, request))
                result.flashing("success" -> "You're all set!")
              }
            }
        }
      }
    )
  }
}
