package services.socket

import java.util.UUID

import akka.actor.{ActorRef, Props}
import models._
import models.user.User
import utils.Logging
import utils.metrics.InstrumentedActor

object SocketService {
  type i18n = (String, Seq[Any]) => String

  def props(id: Option[UUID], supervisor: ActorRef, user: User, out: ActorRef, sourceAddress: String, messages: i18n) = {
    Props(SocketService(id.getOrElse(UUID.randomUUID), supervisor, user, out, sourceAddress, messages))
  }
}

case class SocketService(
    id: UUID, supervisor: ActorRef, user: User, out: ActorRef, sourceAddress: String, messages: SocketService.i18n
) extends InstrumentedActor with RequestMessageHelper with DetailHelper with Logging {

  protected[this] var pendingDebugChannel: Option[ActorRef] = None

  override def preStart() = {
    log.info(s"Starting connection for user [${user.id}: ${user.username}].")
    supervisor ! SocketStarted(user, id, self)
    out ! UserSettings(user.id, user.username, user.profile.providerKey, user.preferences)
  }

  override def postStop() = {
    supervisor ! SocketStopped(id)
  }
}
