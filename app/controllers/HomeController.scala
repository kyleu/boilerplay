package controllers

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import com.mohiva.play.silhouette.api.HandlerResult
import models.auth.Credentials
import models._
import play.api.mvc.{AnyContentAsEmpty, Request, WebSocket}
import util.Logging
import util.metrics.InstrumentedActor
import util.web.{MessageFrameFormatter, WebsocketUtils}

import scala.concurrent.Future

object HomeController {
  case class SocketService(
      id: UUID, supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String
  ) extends InstrumentedActor with Logging {

    override def preStart() = {
      log.info(s"Starting connection for user [${creds.user.id}: ${creds.user.username}].")
      supervisor.tell(SocketStarted(creds, "home", id, self), self)
      out.tell(UserSettings(creds.user.id, creds.user.username, creds.user.profile.providerKey, creds.user.preferences), self)
    }

    override def receiveRequest = {
      case p: Ping => timeReceive(p) { out.tell(Pong(p.ts), self) }
      case rm: ResponseMessage => out.tell(rm, self)
      case x => throw new IllegalArgumentException(s"Unhandled request message [${x.getClass.getSimpleName}].")
    }

    override def postStop() = {
      supervisor.tell(SocketStopped(id), self)
    }
  }

  def props(id: Option[UUID], supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String) = {
    Props(SocketService(id.getOrElse(UUID.randomUUID), supervisor, creds, out, sourceAddress))
  }
}

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (
    override val app: Application, implicit val system: ActorSystem, implicit val materializer: Materializer
) extends BaseController("home") {

  import app.contexts.webContext

  private[this] val formatter = new MessageFrameFormatter()

  def home() = withSession("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.index(request.identity, app.config.debug)))
  }

  def connect(binary: Boolean) = WebSocket.acceptOrResult[RequestMessage, ResponseMessage] { request =>
    implicit val req = Request(request, AnyContentAsEmpty)
    val connId = UUID.randomUUID()
    app.silhouette.SecuredRequestHandler { secured => Future.successful(HandlerResult(Ok, Some(secured.identity))) }.map {
      case HandlerResult(_, Some(user)) => Right(WebsocketUtils.actorRef(connId) { out =>
        HomeController.props(Some(connId), app.supervisor, Credentials(user, request.remoteAddress), out, request.remoteAddress)
      })
      case HandlerResult(_, None) => Left(Redirect(controllers.routes.HomeController.home()).flashing("error" -> "You're not signed in."))
    }
  }(formatter.transformer(binary))

  def untrail(path: String) = Action.async {
    Future.successful(MovedPermanently(s"/$path"))
  }

  def externalLink(url: String) = withSession("external.link") { implicit request => implicit td =>
    Future.successful(Redirect(if (url.startsWith("http")) { url } else { "http://" + url }))
  }

  def ping(timestamp: Long) = withSession("ping") { implicit request => implicit td =>
    Future.successful(Ok(timestamp.toString))
  }

  def robots() = withSession("robots") { implicit request => implicit td =>
    Future.successful(Ok("User-agent: *\nDisallow: /"))
  }
}
