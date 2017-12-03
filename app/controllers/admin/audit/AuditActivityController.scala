package controllers.admin.audit

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.HandlerResult
import controllers.BaseController
import models.auth.Credentials
import models.{Application, RequestMessage, ResponseMessage}
import play.api.mvc.{AnyContentAsEmpty, Request, WebSocket}
import services.audit.AuditSocketService
import util.web.{MessageFrameFormatter, WebsocketUtils}

import scala.concurrent.Future

@javax.inject.Singleton
class AuditActivityController @javax.inject.Inject() (
    override val app: Application, implicit val system: ActorSystem, implicit val materializer: Materializer
) extends BaseController("home") {

  import app.contexts.webContext

  private[this] val formatter = new MessageFrameFormatter()

  def activity() = withSession("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.audit.auditActivity(request.identity, app.config.debug)))
  }

  def connect(binary: Boolean) = WebSocket.acceptOrResult[RequestMessage, ResponseMessage] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    val connId = UUID.randomUUID()
    app.silhouette.SecuredRequestHandler { secured => Future.successful(HandlerResult(Ok, Some(secured.identity))) }.map {
      case HandlerResult(_, Some(user)) => Right(WebsocketUtils.actorRef(connId) { out =>
        AuditSocketService.props(Some(connId), app.supervisor, Credentials(user, request.remoteAddress), out, request.remoteAddress)
      })
      case HandlerResult(_, None) => Left(Redirect(controllers.routes.HomeController.home()).flashing("error" -> "You're not signed in."))
    }
  }(formatter.transformer(binary))
}
