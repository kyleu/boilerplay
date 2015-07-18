package models

import java.util.UUID

import akka.actor.ActorRef
import models.user.User

sealed trait InternalMessage

case class ConnectionStarted(user: User, connectionId: UUID, conn: ActorRef) extends InternalMessage
case class ConnectionStopped(connectionId: UUID) extends InternalMessage

case object GetSystemStatus extends InternalMessage
case class SystemStatus(connections: Seq[(UUID, String)]) extends InternalMessage

case class ConnectionTrace(id: UUID) extends InternalMessage
case class ClientTrace(id: UUID) extends InternalMessage
case class TraceResponse(id: UUID, data: Seq[(String, Any)]) extends InternalMessage
