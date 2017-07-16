package services.socket

import java.util.UUID

import akka.actor.{ActorRef, Props}
import models._
import models.user.User
import util.Logging
import util.metrics.InstrumentedActor

object SocketService {
  def props(id: Option[UUID], supervisor: ActorRef, user: User, out: ActorRef, sourceAddress: String) = {
    Props(SocketService(id.getOrElse(UUID.randomUUID), supervisor, user, out, sourceAddress))
  }
}

case class SocketService(
    id: UUID, supervisor: ActorRef, user: User, out: ActorRef, sourceAddress: String
) extends InstrumentedActor with RequestMessageHelper with Logging {

  override def preStart() = {
    log.info(s"Starting connection for user [${user.id}: ${user.username}].")
    supervisor ! SocketStarted(user, id, self)
    out ! UserSettings(user.id, user.username, user.profile.providerKey, user.preferences)
  }

  override def postStop() = {
    supervisor ! SocketStopped(id)
  }
}
