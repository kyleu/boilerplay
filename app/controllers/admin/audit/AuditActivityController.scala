package controllers.admin.audit

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.HandlerResult
import controllers.BaseController
import models.{Application, RequestMessage, ResponseMessage}
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AnyContentAsEmpty, Request, WebSocket}
import services.socket.SocketService
import util.web.MessageFrameFormatter

import scala.concurrent.Future

@javax.inject.Singleton
class AuditActivityController @javax.inject.Inject() (
    override val app: Application, implicit val system: ActorSystem, implicit val materializer: Materializer
) extends BaseController("home") {

  import app.contexts.webContext

  private[this] implicit val t = new MessageFrameFormatter(app.config.debug).transformer

  def activity() = withSession("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.audit.auditActivity(request.identity, app.config.debug)))
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
}
