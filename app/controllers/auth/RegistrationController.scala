package controllers.auth

import com.mohiva.play.silhouette.api.{ LoginEvent, LoginInfo, SignUpEvent }
import com.mohiva.play.silhouette.impl.providers.{ CommonSocialProfile, CredentialsProvider }
import controllers.BaseController
import models.user.{ RegistrationData, UserForms }
import play.api.i18n.{ Messages, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.AnyContent
import services.user.AuthenticationEnvironment

import scala.concurrent.Future

@javax.inject.Singleton
class RegistrationController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment
) extends BaseController {

  def registrationForm = withSession("form") { implicit request =>
    Future.successful(Ok(views.html.auth.register(request.identity, UserForms.registrationForm)))
  }

  def register = withSession("register") { implicit request =>
    UserForms.registrationForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.register(request.identity, form))),
      data => {
        env.identityService.retrieve(LoginInfo(CredentialsProvider.ID, data.email)).flatMap {
          case Some(user) => Future.successful {
            Ok(views.html.auth.register(request.identity, UserForms.registrationForm.fill(data))).flashing("error" -> Messages("registration.email.taken"))
          }
          case None => env.identityService.retrieve(data.username) flatMap {
            case Some(user) => Future.successful {
              Ok(views.html.auth.register(request.identity, UserForms.registrationForm.fill(data))).flashing("error" -> Messages("registration.username.taken"))
            }
            case None => saveProfile(data)
          }
        }
      }
    )
  }

  private[this] def saveProfile(data: RegistrationData)(implicit request: SecuredRequest[AnyContent]) = {
    if (request.identity.profiles.exists(_.providerID == "credentials")) {
      throw new IllegalStateException("You're already registered.") // TODO Fix?
    }

    val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
    val authInfo = env.hasher.hash(data.password)
    val user = request.identity.copy(
      username = if (data.username.isEmpty) { request.identity.username } else { Some(data.username) },
      profiles = request.identity.profiles :+ loginInfo
    )
    val profile = CommonSocialProfile(
      loginInfo = loginInfo,
      email = Some(data.email)
    )
    val r = Redirect(controllers.routes.HomeController.index())
    for {
      avatar <- env.avatarService.retrieveURL(data.email)
      profile <- env.userService.create(user, profile.copy(avatarURL = avatar.orElse(Some("default"))))
      u <- env.userService.save(
        user.copy(preferences = user.preferences.copy(avatar = avatar.getOrElse(user.preferences.avatar))),
        update = true
      )
      authInfo <- env.authInfoService.save(loginInfo, authInfo)
      authenticator <- env.authenticatorService.create(loginInfo)
      value <- env.authenticatorService.init(authenticator)
      result <- env.authenticatorService.embed(value, r)
    } yield {
      env.eventBus.publish(SignUpEvent(u, request, request2Messages))
      env.eventBus.publish(LoginEvent(u, request, request2Messages))
      result
    }
  }
}
