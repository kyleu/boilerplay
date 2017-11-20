package services.supervisor

import java.util.UUID

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{ActorRef, OneForOneStrategy, SupervisorStrategy}
import models._
import java.time.LocalDateTime

import models.auth.Credentials
import models.supervisor.SocketDescription
import util.metrics.{InstrumentedActor, MetricsServletActor}
import util.{DateUtils, Logging}

object ActorSupervisor {
  case class Broadcast(channel: String, msg: ResponseMessage)
  case class SocketRecord(socketId: UUID, userId: UUID, name: String, channel: String, actorRef: ActorRef, started: LocalDateTime) {
    val desc = SocketDescription(socketId, userId, name, channel, started)
  }

  private def emptyMap = collection.mutable.HashMap.empty[UUID, ActorSupervisor.SocketRecord]
}

class ActorSupervisor(val app: Application) extends InstrumentedActor with Logging {
  private[this] val sockets = collection.mutable.HashMap.empty[String, collection.mutable.HashMap[UUID, ActorSupervisor.SocketRecord]]
  private[this] def socketById(id: UUID) = sockets.values.find(_.contains(id)).flatMap(_.get(id))
  private[this] val socketsCounter = metrics.counter("active-connections")

  override def preStart() = {
    context.actorOf(MetricsServletActor.props(app.config.metrics), "metrics-servlet")
    log.debug(s"Actor Supervisor started for [${util.Config.projectId}].")
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Stop
  }

  override def receiveRequest = {
    case ss: SocketStarted => timeReceive(ss) { handleSocketStarted(ss.creds, ss.channel, ss.socketId, ss.conn) }
    case ss: SocketStopped => timeReceive(ss) { handleSocketStopped(ss.socketId) }

    case GetSystemStatus => timeReceive(GetSystemStatus) { handleGetSystemStatus() }
    case ct: SendSocketTrace => timeReceive(ct) { handleSendSocketTrace(ct) }
    case ct: SendClientTrace => timeReceive(ct) { handleSendClientTrace(ct) }

    case ActorSupervisor.Broadcast(channel, msg) => sockets.getOrElse(channel, ActorSupervisor.emptyMap).foreach(_._2.actorRef ! msg)

    case im: InternalMessage => log.warn(s"Unhandled internal message [${im.getClass.getSimpleName}] received.")
    case x => log.warn(s"ActorSupervisor encountered unknown message: ${x.toString}")
  }

  private[this] def handleGetSystemStatus() = {
    val channelStatuses = sockets.mapValues(_.toList.map(x => x._2.desc).sortBy(_.name)).toSeq.sortBy(_._1)
    sender() ! SystemStatus(channelStatuses)
  }

  private[this] def handleSendSocketTrace(ct: SendSocketTrace) = socketById(ct.id) match {
    case Some(c) => c.actorRef forward ct
    case None => sender() ! ServerError("Unknown Socket", ct.id.toString)
  }

  private[this] def handleSendClientTrace(ct: SendClientTrace) = socketById(ct.id) match {
    case Some(c) => c.actorRef forward ct
    case None => sender() ! ServerError("Unknown Client Socket", ct.id.toString)
  }

  protected[this] def handleSocketStarted(creds: Credentials, channel: String, socketId: UUID, socket: ActorRef) = {
    log.debug(s"Socket [$socketId] registered to [${creds.user.username}] with path [${socket.path}].")
    sockets.getOrElseUpdate(channel, ActorSupervisor.emptyMap)(socketId) = {
      ActorSupervisor.SocketRecord(socketId, creds.user.id, creds.user.username, channel, socket, DateUtils.now)
    }
    socketsCounter.inc()
  }

  protected[this] def handleSocketStopped(id: UUID) = {
    sockets.foreach { channel =>
      channel._2.remove(id).foreach { sock =>
        log.debug(s"Connection [$id] [${sock.actorRef.path}] removed from channel [${channel._1}].")
      }
    }
    socketsCounter.dec()
  }
}
