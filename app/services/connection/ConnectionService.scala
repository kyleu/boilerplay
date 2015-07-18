package services.connection

import java.util.UUID

import akka.actor.{ ActorRef, Props }
import models._
import models.user.User
import utils.Config

object ConnectionService {
  def props(supervisor: ActorRef, user: User, out: ActorRef) = Props(new ConnectionService(supervisor, user, out))
}

class ConnectionService(val supervisor: ActorRef, val user: User, val out: ActorRef) extends ConnectionServiceHelper {
  protected[this] val id = UUID.randomUUID

  protected[this] var userPreferences = user.preferences
  protected[this] var pendingDebugChannel: Option[ActorRef] = None

  override def preStart() = {
    supervisor ! ConnectionStarted(user, id, self)
  }

  override def receiveRequest = {
    // Incoming basic messages
    case mr: MalformedRequest => timeReceive(mr) { log.error(s"MalformedRequest:  [${mr.reason}]: [${mr.content}].") }
    case p: Ping => timeReceive(p) { out ! Pong(p.timestamp) }
    case GetVersion => timeReceive(GetVersion) { out ! VersionResponse(Config.version) }
    case sp: SetPreference => timeReceive(sp) { handleSetPreference(sp) }
    case di: DebugInfo => timeReceive(di) { handleDebugInfo(di.data) }

    // Incoming game messages
    case im: InternalMessage => handleInternalMessage(im)

    // Outgoing messages
    case rm: ResponseMessage => handleResponseMessage(rm)

    case x => throw new IllegalArgumentException(s"Unhandled message [${x.getClass.getSimpleName}].")
  }

  override def postStop() = {
    supervisor ! ConnectionStopped(id)
  }

  private[this] def handleInternalMessage(im: InternalMessage) = im match {
    case ct: ConnectionTrace => timeReceive(ct) { handleConnectionTrace() }
    case ct: ClientTrace => timeReceive(ct) { handleClientTrace() }
    case x => throw new IllegalArgumentException(s"Unhandled internal message [${x.getClass.getSimpleName}].")
  }
}
