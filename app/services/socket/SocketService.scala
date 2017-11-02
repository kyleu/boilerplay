package services.socket

import java.util.UUID

import akka.actor.{ActorRef, Props}
import models._
import models.auth.Credentials
import util.Logging
import util.metrics.InstrumentedActor

object SocketService {
  def props(id: Option[UUID], channel: String, supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String) = {
    Props(SocketService(id.getOrElse(UUID.randomUUID), channel, supervisor, creds, out, sourceAddress))
  }
}

case class SocketService(
    id: UUID, channel: String, supervisor: ActorRef, creds: Credentials, out: ActorRef, sourceAddress: String
) extends InstrumentedActor with RequestMessageHelper with Logging {

  override def preStart() = {
    log.info(s"Starting connection for user [${creds.user.id}: ${creds.user.username}].")
    supervisor ! SocketStarted(creds, channel, id, self)
    out ! UserSettings(creds.user.id, creds.user.username, creds.user.profile.providerKey, creds.user.preferences)
  }

  override def postStop() = {
    supervisor ! SocketStopped(id)
  }
}
