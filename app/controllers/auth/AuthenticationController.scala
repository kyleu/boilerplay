package controllers.auth

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{LoginEvent, LogoutEvent}
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.BaseController
import models.Application
import models.user.UserForms
import services.user.UserSearchService

import scala.concurrent.Future

@javax.inject.Singleton
class AuthenticationController @javax.inject.Inject() (
    override val app: Application,
    userSearchService: UserSearchService,
    credentialsProvider: CredentialsProvider
) extends BaseController("authentication") {
  import app.contexts.webContext

  def signInForm = withoutSession("form") { implicit request => implicit td =>
    //val src = request.headers.get("Referer").filter(_.contains(request.host))
    val resp = Ok(views.html.auth.signin(request.identity, UserForms.signInForm, app.settingsService.allowRegistration))
    Future.successful(resp)
  }

  def authenticateCredentials = withoutSession("authenticate") { implicit request => implicit td =>
    UserForms.signInForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.signin(request.identity, form, app.settingsService.allowRegistration))),
      credentials => {
        val creds = credentials.copy(identifier = credentials.identifier.toLowerCase)
        credentialsProvider.authenticate(creds).flatMap { loginInfo =>
          val result = request.session.get("returnUrl") match {
            case Some(url) => Redirect(url).withSession(request.session - "returnUrl")
            case None => Redirect(controllers.routes.HomeController.home())
          }
          userSearchService.getByLoginInfo(loginInfo).flatMap {
            case Some(user) => app.silhouette.env.authenticatorService.create(loginInfo).flatMap { authenticator =>
              app.silhouette.env.eventBus.publish(LoginEvent(user, request))
              app.silhouette.env.authenticatorService.init(authenticator).flatMap { v =>
                app.silhouette.env.authenticatorService.embed(v, result).map { x =>
                  x
                }
              }
            }
            case None => Future.failed(new IdentityNotFoundException(s"Couldn't find user [${loginInfo.providerID}]."))
          }
        }.recover {
          case _: ProviderException => Redirect(controllers.auth.routes.AuthenticationController.signInForm()).flashing("error" -> "Invalid credentials")
        }
      }
    )
  }

  def signOut = withSession("signout") { implicit request => implicit td =>
    implicit val result = Redirect(controllers.routes.HomeController.home())
    app.silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    app.silhouette.env.authenticatorService.discard(request.authenticator, result)
  }
}
