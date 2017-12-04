package services.audit

import java.util.UUID

import akka.actor.{ActorRef, Props}
import models._
import models.auth.Credentials
import util.{Config, Logging}
import util.metrics.InstrumentedActor

object AuditSocketService {
  def props(id: Option[UUID], supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String) = {
    Props(AuditSocketService(id.getOrElse(UUID.randomUUID), supervisor, creds, out, sourceAddress))
  }
}

case class AuditSocketService(id: UUID, supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String) extends InstrumentedActor with Logging {
  override def preStart() = {
    log.info(s"Starting connection for user [${creds.user.id}: ${creds.user.username}].")
    supervisor.tell(SocketStarted(creds, "audit", id, self), self)
  }

  override def receiveRequest = {
    case mr: MalformedRequest => timeReceive(mr) { log.error(s"MalformedRequest:  [${mr.reason}]: [${mr.content}].") }

    case p: Ping => timeReceive(p) { out.tell(Pong(p.ts), self) }
    case gv: GetVersion => timeReceive(gv) { out.tell(VersionResponse(Config.version), self) }

    case im: InternalMessage => handleInternalMessage(im)
    case rm: ResponseMessage => out.tell(rm, self)
    case x => throw new IllegalArgumentException(s"Unhandled request message [${x.getClass.getSimpleName}].")
  }

  private[this] def handleInternalMessage(im: InternalMessage) = im match {
    case x => throw new IllegalArgumentException(s"Unhandled internal message [${x.getClass.getSimpleName}].")
  }

  override def postStop() = {
    supervisor.tell(SocketStopped(id), self)
  }
}
