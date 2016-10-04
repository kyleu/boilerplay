package controllers.auth

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.BaseController
import models.settings.SettingKey
import models.user._
import play.api.i18n.Messages
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.settings.SettingsService
import services.user.{UserSearchService, UserService}
import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class RegistrationController @javax.inject.Inject() (
    override val ctx: ApplicationContext,
    userService: UserService,
    userSearchService: UserSearchService,
    authInfoRepository: AuthInfoRepository,
    hasher: PasswordHasher
) extends BaseController {
  def registrationForm(email: Option[String] = None) = withoutSession("form") { implicit request =>
    if (SettingsService.allowRegistration) {
      val form = UserForms.registrationForm.fill(RegistrationData(
        username = email.map(e => if (e.contains('@')) { e.substring(0, e.indexOf('@')) } else { "" }).getOrElse(""),
        email = email.getOrElse("")
      ))
      Future.successful(Ok(views.html.auth.register(request.identity, form)))
    } else {
      Future.successful(Redirect(controllers.routes.HomeController.home()).flashing("error" -> messagesApi("registration.disabled")))
    }
  }

  def register = withoutSession("register") { implicit request =>
    if (!SettingsService.allowRegistration) {
      throw new IllegalStateException(messagesApi("error.cannot.sign.up"))
    }
    UserForms.registrationForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.register(request.identity, form))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email.toLowerCase)
        userSearchService.retrieve(loginInfo).flatMap {
          case _ if data.password != data.passwordConfirm => Future.successful(
            Redirect(controllers.auth.routes.RegistrationController.register()).flashing("error" -> Messages("registration.passwords.do.not.match"))
          )
          case Some(user) => Future.successful(
            Redirect(controllers.auth.routes.RegistrationController.register()).flashing("error" -> Messages("registration.email.taken"))
          )
          case None =>
            val authInfo = hasher.hash(data.password)
            val role = Role.withName(SettingsService(SettingKey.DefaultNewUserRole))
            val user = User(
              id = UUID.randomUUID,
              username = data.username,
              preferences = UserPreferences.empty,
              profile = loginInfo,
              role = role
            )
            val userSavedFuture = userService.save(user)
            val result = request.session.get("returnUrl") match {
              case Some(url) => Redirect(url).withSession(request.session - "returnUrl")
              case None => Redirect(controllers.routes.HomeController.home())
            }
            for {
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- ctx.silhouette.env.authenticatorService.create(loginInfo)
              value <- ctx.silhouette.env.authenticatorService.init(authenticator)
              result <- ctx.silhouette.env.authenticatorService.embed(value, result)
              userSaved <- userSavedFuture
            } yield {
              ctx.silhouette.env.eventBus.publish(SignUpEvent(userSaved, request))
              ctx.silhouette.env.eventBus.publish(LoginEvent(userSaved, request))
              result.flashing("success" -> "You're all set!")
            }
        }
      }
    )
  }
}
