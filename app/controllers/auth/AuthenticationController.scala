package controllers.auth

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.{LoginEvent, LogoutEvent}
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.BaseController
import models.user.UserForms
import play.api.i18n.{Lang, Messages}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.user.UserSearchService
import utils.ApplicationContext

import scala.concurrent.Future

@javax.inject.Singleton
class AuthenticationController @javax.inject.Inject() (
    override val ctx: ApplicationContext,
    userSearchService: UserSearchService,
    credentialsProvider: CredentialsProvider
) extends BaseController {
  def signInForm = withoutSession("form") { implicit request =>
    val src = request.headers.get("Referer").filter(_.contains(request.host))
    val resp = Ok(views.html.auth.signin(request.identity, UserForms.signInForm))
    Future.successful(resp)
  }

  def authenticateCredentials = withoutSession("authenticate") { implicit request =>
    UserForms.signInForm.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.auth.signin(request.identity, form))),
      credentials => {
        val creds = credentials.copy(identifier = credentials.identifier.toLowerCase)
        credentialsProvider.authenticate(creds).flatMap { loginInfo =>
          val result = request.session.get("returnUrl") match {
            case Some(url) => Redirect(url).withSession(request.session - "returnUrl")
            case None => Redirect(controllers.routes.HomeController.home())
          }
          userSearchService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              ctx.silhouette.env.authenticatorService.create(loginInfo).flatMap { authenticator =>
                ctx.silhouette.env.eventBus.publish(LoginEvent(user, request))
                ctx.silhouette.env.authenticatorService.init(authenticator).flatMap { v =>
                  ctx.silhouette.env.authenticatorService.embed(v, result.withLang(Lang(user.preferences.language.code)))
                }
              }
            case None => Future.failed(new IdentityNotFoundException(messagesApi("error.missing.user", loginInfo.providerID)))
          }
        }.recover {
          case e: ProviderException =>
            Redirect(controllers.auth.routes.AuthenticationController.signInForm()).flashing("error" -> Messages("authentication.invalid.credentials"))
        }
      }
    )
  }

  def signOut = withSession("signout") { implicit request =>
    implicit val result = Redirect(controllers.routes.HomeController.home())

    ctx.silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    ctx.silhouette.env.authenticatorService.discard(request.authenticator, result.clearingLang)
  }
}
