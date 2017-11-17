package models

import java.util.UUID

import akka.actor.ActorRef
import models.auth.Credentials
import services.supervisor.ActorSupervisor

sealed trait InternalMessage

case class SocketStarted(creds: Credentials, channel: String, socketId: UUID, conn: ActorRef) extends InternalMessage
case class SocketStopped(socketId: UUID) extends InternalMessage

case object GetSystemStatus extends InternalMessage
case class SystemStatus(sockets: Seq[ActorSupervisor.SocketDescription]) extends InternalMessage

case class SendSocketTrace(id: UUID) extends InternalMessage
case class SocketTraceResponse(id: UUID, userId: UUID, username: String) extends InternalMessage

case class SendClientTrace(id: UUID) extends InternalMessage
case class ClientTraceResponse(id: UUID, data: String) extends InternalMessage
