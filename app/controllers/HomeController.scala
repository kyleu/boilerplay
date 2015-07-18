package controllers

import play.api.i18n.MessagesApi
import play.api.mvc.Action
import services.email.EmailService
import services.user.AuthenticationEnvironment

import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment,
    emailService: EmailService
) extends BaseController {
  def index() = withSession("index") { implicit request =>
    Future.successful(Ok(views.html.index(request.identity)))
  }

  def untrail(path: String) = Action.async {
    Future.successful(MovedPermanently(s"/$path"))
  }

  def externalLink(url: String) = withSession("external.link") { implicit request =>
    Future.successful(Redirect(if (url.startsWith("http")) { url } else { "http://" + url }))
  }

  def ping(timestamp: Long) = withSession("ping") { implicit request =>
    Future.successful(Ok(timestamp.toString))
  }
}
