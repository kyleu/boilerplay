package controllers

import akka.actor.ActorRef
import models.{ RequestMessage, ResponseMessage }
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ AnyContentAsEmpty, Request, WebSocket }
import services.connection.ConnectionService
import services.supervisor.ActorSupervisor
import services.user.AuthenticationEnvironment
import utils.play.MessageFrameFormatter

import scala.concurrent.Future

@javax.inject.Singleton
class WebsocketController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment
) extends BaseController {
  import play.api.Play.current
  import MessageFrameFormatter._

  val supervisor = ActorSupervisor.instance

  def connect() = WebSocket.tryAcceptWithActor[RequestMessage, ResponseMessage] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    SecuredRequestHandler { securedRequest =>
      Future.successful(HandlerResult(Ok, Some(securedRequest.identity)))
    }.map {
      case HandlerResult(r, Some(user)) => Right(ConnectionService.props(supervisor, user, _: ActorRef))
      case HandlerResult(r, None) => Left(r)
    }
  }
}
