package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.HandlerResult
import models.{RequestMessage, ResponseMessage}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Action, AnyContentAsEmpty, Request, WebSocket}
import services.socket.SocketService
import utils.Application
import utils.web.MessageFrameFormatter

import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (
    override val app: Application,
    implicit val system: ActorSystem,
    implicit val materializer: Materializer
) extends BaseController {
  private[this] implicit val t = new MessageFrameFormatter(app.config.debug).transformer

  def home() = withSession("home") { implicit request =>
    Future.successful(Ok(views.html.index(request.identity, app.config.debug)))
  }

  def connect() = WebSocket.acceptOrResult[RequestMessage, ResponseMessage] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    app.silhouette.SecuredRequestHandler { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Right(ActorFlow.actorRef { out =>
        SocketService.props(None, app.supervisor, user, out, request.remoteAddress)
      })
    }
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

  def robots() = withSession("robots") { implicit request =>
    Future.successful(Ok("User-agent: *\nDisallow: /"))
  }
}
