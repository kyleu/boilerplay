package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.HandlerResult
import models.{RequestMessage, ResponseMessage}
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AnyContentAsEmpty, Request, WebSocket}
import play.twirl.api.HtmlFormat
import services.socket.SocketService
import util.Application
import util.tracing.TraceData
import util.web.MessageFrameFormatter

import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (
    override val app: Application,
    implicit val system: ActorSystem,
    implicit val materializer: Materializer
) extends BaseController {
  import app.contexts.webContext

  private[this] implicit val t = new MessageFrameFormatter(app.config.debug).transformer

  def doStuff()(implicit traceData: TraceData) = {
    app.tracing.trace("test-uno") { implicit data =>
      println("!!!!")
    }
  }

  def home() = withSession("home") { implicit request =>
    doStuff()
    Future.successful(Ok(views.html.index(request.identity, app.config.debug)))
  }

  def connect() = WebSocket.acceptOrResult[RequestMessage, ResponseMessage] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    app.silhouette.SecuredRequestHandler { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(_, Some(user)) => Right(ActorFlow.actorRef { out =>
        SocketService.props(None, app.supervisor, user, out, request.remoteAddress)
      })
      case HandlerResult(_, None) => Left(Redirect(controllers.routes.HomeController.home()).flashing("error" -> "You're not signed in."))
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
