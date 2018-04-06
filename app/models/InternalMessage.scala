package models

import java.util.UUID

import akka.actor.ActorRef
import models.auth.Credentials
import models.supervisor.SocketDescription

sealed trait InternalMessage

object InternalMessage {
  final case class SocketStarted(creds: Credentials, channel: String, socketId: UUID, conn: ActorRef) extends InternalMessage
  final case class SocketStopped(socketId: UUID) extends InternalMessage
  case object GetSystemStatus extends InternalMessage
  final case class SystemStatus(channels: Seq[(String, Seq[SocketDescription])]) extends InternalMessage
  final case class SendSocketTrace(id: UUID) extends InternalMessage
  final case class SocketTraceResponse(id: UUID, userId: UUID, username: String) extends InternalMessage
  final case class SendClientTrace(id: UUID) extends InternalMessage
  final case class ClientTraceResponse(id: UUID, data: String) extends InternalMessage
}
