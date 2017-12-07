package services.audit

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import io.prometheus.client.{Counter, Histogram}
import models._
import models.auth.Credentials
import util.{Config, Logging}
import util.metrics.Instrumented

object AuditSocketService {
  def props(id: Option[UUID], supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String) = {
    Props(AuditSocketService(id.getOrElse(UUID.randomUUID), supervisor, creds, out, sourceAddress))
  }

  private lazy val metricsName = util.Config.projectId + "_audit_socket_service"
  private lazy val receiveHistogram = Histogram.build(metricsName + "_receive", s"Message metrics for [$metricsName]").labelNames("msg").register()
  private lazy val errorCounter = Counter.build(metricsName + "_exception", s"Exception metrics for [$metricsName]").labelNames("msg", "ex").register()
}

case class AuditSocketService(id: UUID, supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String) extends Actor with Logging {
  override def preStart() = {
    log.info(s"Starting connection for user [${creds.user.id}: ${creds.user.username}].")
    supervisor.tell(SocketStarted(creds, "audit", id, self), self)
  }

  private[this] def time(msg: Any, f: => Unit) = Instrumented.timeReceive(msg, AuditSocketService.receiveHistogram, AuditSocketService.errorCounter)(f)

  override def receive = {
    case mr: MalformedRequest => time(mr, log.error(s"MalformedRequest:  [${mr.reason}]: [${mr.content}]."))

    case p: Ping => time(p, out.tell(Pong(p.ts), self))
    case gv: GetVersion => time(gv, out.tell(VersionResponse(Config.version), self))

    case im: InternalMessage => time(im, handleInternalMessage(im))
    case rm: ResponseMessage => time(rm, out.tell(rm, self))
    case x => throw new IllegalArgumentException(s"Unhandled request message [${x.getClass.getSimpleName}].")
  }

  private[this] def handleInternalMessage(im: InternalMessage) = im match {
    case x => throw new IllegalArgumentException(s"Unhandled internal message [${x.getClass.getSimpleName}].")
  }

  override def postStop() = {
    supervisor.tell(SocketStopped(id), self)
  }
}
