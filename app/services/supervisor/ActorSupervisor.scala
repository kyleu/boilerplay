package services.supervisor

import java.util.UUID

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{ActorRef, OneForOneStrategy, SupervisorStrategy}
import models._
import models.user.User
import java.time.LocalDateTime
import util.metrics.{InstrumentedActor, MetricsServletActor}
import util.{DateUtils, Logging}

object ActorSupervisor {
  case class SocketRecord(userId: UUID, name: String, actorRef: ActorRef, started: LocalDateTime)
  protected val sockets = collection.mutable.HashMap.empty[UUID, SocketRecord]
}

class ActorSupervisor(val app: Application) extends InstrumentedActor with Logging {
  import services.supervisor.ActorSupervisor._

  protected[this] val socketsCounter = metrics.counter("active-connections")

  override def preStart() = {
    context.actorOf(MetricsServletActor.props(app.config.metrics), "metrics-servlet")
    log.debug(s"Actor Supervisor started for [${util.Config.projectId}].")
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Stop
  }

  override def receiveRequest = {
    case ss: SocketStarted => timeReceive(ss) { handleSocketStarted(ss.user, ss.socketId, ss.conn) }
    case ss: SocketStopped => timeReceive(ss) { handleSocketStopped(ss.socketId) }

    case GetSystemStatus => timeReceive(GetSystemStatus) { handleGetSystemStatus() }
    case ct: SendSocketTrace => timeReceive(ct) { handleSendSocketTrace(ct) }
    case ct: SendClientTrace => timeReceive(ct) { handleSendClientTrace(ct) }

    case im: InternalMessage => log.warn(s"Unhandled internal message [${im.getClass.getSimpleName}] received.")
    case x => log.warn(s"ActorSupervisor encountered unknown message: ${x.toString}")
  }

  private[this] def handleGetSystemStatus() = {
    val connectionStatuses = ActorSupervisor.sockets.toList.sortBy(_._2.name).map(x => x._1 -> x._2.name)
    sender() ! SystemStatus(connectionStatuses)
  }

  private[this] def handleSendSocketTrace(ct: SendSocketTrace) = ActorSupervisor.sockets.find(_._1 == ct.id) match {
    case Some(c) => c._2.actorRef forward ct
    case None => sender() ! ServerError("Unknown Socket", ct.id.toString)
  }

  private[this] def handleSendClientTrace(ct: SendClientTrace) = ActorSupervisor.sockets.find(_._1 == ct.id) match {
    case Some(c) => c._2.actorRef forward ct
    case None => sender() ! ServerError("Unknown Client Socket", ct.id.toString)
  }

  protected[this] def handleSocketStarted(user: User, socketId: UUID, socket: ActorRef) = {
    log.debug(s"Socket [$socketId] registered to [${user.username}] with path [${socket.path}].")
    ActorSupervisor.sockets(socketId) = SocketRecord(user.id, user.username, socket, DateUtils.now)
    socketsCounter.inc()
  }

  protected[this] def handleSocketStopped(id: UUID) = {
    ActorSupervisor.sockets.remove(id) match {
      case Some(sock) =>
        socketsCounter.dec()
        log.debug(s"Connection [$id] [${sock.actorRef.path}] stopped.")
      case None => log.warn(s"Socket [$id] stopped but is not registered.")
    }
  }
}
