package services.supervisor

import java.util.UUID

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, OneForOneStrategy, SupervisorStrategy}
import java.time.LocalDateTime

import models.InternalMessage._
import models.ResponseMessage.ServerError
import models.{Application, InternalMessage, ResponseMessage}
import models.auth.Credentials
import models.supervisor.SocketDescription
import util.metrics.Instrumented
import util.{DateUtils, Logging}

object ActorSupervisor {
  final case class Broadcast(channel: String, msg: ResponseMessage)
  final case class SocketRecord(socketId: UUID, userId: UUID, name: String, channel: String, actorRef: ActorRef, started: LocalDateTime) {
    val desc = SocketDescription(socketId, userId, name, channel, started)
  }

  private def emptyMap = collection.mutable.HashMap.empty[UUID, ActorSupervisor.SocketRecord]
}

class ActorSupervisor(val app: Application) extends Actor with Logging {
  private[this] val sockets = collection.mutable.HashMap.empty[String, collection.mutable.HashMap[UUID, ActorSupervisor.SocketRecord]]
  private[this] def socketById(id: UUID) = sockets.values.find(_.contains(id)).flatMap(_.get(id))

  private[this] lazy val metricsName = util.Config.projectId + "_actor_supervisor"

  override def preStart() = {
    log.debug(s"Actor Supervisor started for [${util.Config.projectId}].")
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Stop
  }

  private[this] def time(msg: Any, f: => Unit) = Instrumented.timeReceive(msg, metricsName, "class", msg.getClass.getSimpleName)(f)

  override def receive = {
    case ss: SocketStarted => time(ss, handleSocketStarted(ss.creds, ss.channel, ss.socketId, ss.conn))
    case ss: SocketStopped => time(ss, handleSocketStopped(ss.socketId))

    case GetSystemStatus => time(GetSystemStatus, handleGetSystemStatus())
    case sst: SendSocketTrace => time(sst, handleSendSocketTrace(sst))
    case sct: SendClientTrace => time(sct, handleSendClientTrace(sct))

    case b: ActorSupervisor.Broadcast => time(b, sockets.getOrElse(b.channel, ActorSupervisor.emptyMap).foreach(_._2.actorRef.tell(b.msg, self)))

    case im: InternalMessage => log.warn(s"Unhandled internal message [${im.getClass.getSimpleName}] received.")
    case x => log.warn(s"ActorSupervisor encountered unknown message: ${x.toString}")
  }

  private[this] def handleGetSystemStatus() = {
    val channelStatuses = sockets.mapValues(_.toList.map(x => x._2.desc).sortBy(_.name)).toSeq.sortBy(_._1)
    sender().tell(SystemStatus(channelStatuses), self)
  }

  private[this] def handleSendSocketTrace(ct: SendSocketTrace) = socketById(ct.id) match {
    case Some(c) => c.actorRef forward ct
    case None => sender().tell(ServerError("Unknown Socket", ct.id.toString), self)
  }

  private[this] def handleSendClientTrace(ct: SendClientTrace) = socketById(ct.id) match {
    case Some(c) => c.actorRef forward ct
    case None => sender().tell(ServerError("Unknown Client Socket", ct.id.toString), self)
  }

  protected[this] def handleSocketStarted(creds: Credentials, channel: String, socketId: UUID, socket: ActorRef) = {
    log.debug(s"Socket [$socketId] registered to [${creds.user.username}] with path [${socket.path}].")
    sockets.getOrElseUpdate(channel, ActorSupervisor.emptyMap)(socketId) = {
      ActorSupervisor.SocketRecord(socketId, creds.user.id, creds.user.username, channel, socket, DateUtils.now)
    }
    Instrumented.regOpt.foreach(_.counter(metricsName + "_sockets").increment())
  }

  protected[this] def handleSocketStopped(id: UUID) = {
    sockets.foreach { channel =>
      channel._2.remove(id).foreach { sock =>
        log.debug(s"Connection [$id] [${sock.actorRef.path}] removed from channel [${channel._1}].")
      }
    }
  }
}
