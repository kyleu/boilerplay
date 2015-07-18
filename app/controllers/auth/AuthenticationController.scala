package controllers.auth

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{ LoginEvent, LogoutEvent }
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.{ CommonSocialProfile, CommonSocialProfileBuilder, SocialProvider }
import controllers.BaseController
import models.user.{ User, UserForms }
import play.api.i18n.{ Messages, MessagesApi }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.user.AuthenticationEnvironment

import scala.concurrent.Future

@javax.inject.Singleton
class AuthenticationController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment
) extends BaseController {
  def signInForm = withSession("form") { implicit request =>
    Future.successful(Ok(views.html.auth.signin(request.identity, UserForms.signInForm)))
  }

  def authenticateCredentials = withSession("authenticate") { implicit request =>
    UserForms.signInForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.signin(request.identity, form))),
      credentials => env.credentials.authenticate(credentials).flatMap { loginInfo =>
        val result = Redirect(controllers.routes.HomeController.index())
        env.identityService.retrieve(loginInfo).flatMap {
          case Some(user) => env.authenticatorService.create(loginInfo).flatMap { authenticator =>
            env.eventBus.publish(LoginEvent(user, request, request2Messages))
            env.authenticatorService.init(authenticator).flatMap(v => env.authenticatorService.embed(v, result))
          }
          case None => Future.failed(new IdentityNotFoundException("Couldn't find user."))
        }
      }.recover {
        case e: ProviderException =>
          Redirect(controllers.auth.routes.AuthenticationController.signInForm()).flashing("error" -> Messages("authentication.invalid.credentials"))
      }
    )
  }

  def authenticateSocial(provider: String) = withSession("authenticate.social") { implicit request =>
    (env.providersMap.get(provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            user <- env.userService.create(mergeUser(request.identity, profile), profile)
            authInfo <- env.authInfoService.save(profile.loginInfo, authInfo)
            authenticator <- env.authenticatorService.create(profile.loginInfo)
            value <- env.authenticatorService.init(authenticator)
            result <- env.authenticatorService.embed(value, Redirect(controllers.routes.HomeController.index()))
          } yield {
            env.eventBus.publish(LoginEvent(user, request, request2Messages))
            result
          }
        }
      case _ => Future.failed(new ProviderException(Messages("authentication.invalid.provider", provider)))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Redirect(routes.AuthenticationController.signInForm()).flashing("error" -> Messages("authentication.service.error", provider))
    }
  }

  def signOut = withSession("signout") { implicit request =>
    val result = Redirect(controllers.routes.HomeController.index())
    env.eventBus.publish(LogoutEvent(request.identity, request, request2Messages))
    env.authenticatorService.discard(request.authenticator, result).map(x => result)
  }

  private[this] def mergeUser(user: User, profile: CommonSocialProfile) = {
    user.copy(
      username = if (profile.firstName.isDefined && user.username.isEmpty) { profile.firstName } else { user.username },
      preferences = user.preferences.copy(avatar = profile.avatarURL.getOrElse(user.preferences.avatar))
    )
  }
}
