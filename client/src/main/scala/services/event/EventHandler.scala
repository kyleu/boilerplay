package services.event

import models._
import services.{Logging, NotificationService}
import services.socket.NetworkMessage
import services.ui.UserManager
import util.DateUtils

import scala.scalajs.js.timers.setTimeout

trait EventHandler {
  private[this] def sendPing(): Unit = {
    NetworkMessage.sendMessage(Ping(DateUtils.now))
    setTimeout(10000)(sendPing())
  }

  def onConnect(): Unit = {
    Logging.debug("Socket connected.")
    setTimeout(1000)(sendPing())
  }

  def onMessage(msg: ResponseMessage): Unit = msg match {
    case p: Pong => NetworkMessage.latencyMs = Some((System.currentTimeMillis - DateUtils.toMillis(p.timestamp)).toInt)
    case us: UserSettings => UserManager.onUserSettings(us)
    case se: ServerError => NotificationService.error(se.reason, se.content)
    case _ => Logging.warn(s"Received unknown message of type [${msg.getClass.getSimpleName}].")
  }

  def onError(err: String): Unit = {
    Logging.error(s"Socket error: [$err]")
  }

  def onClose(): Unit = {
    Logging.info("Socket closed.")
  }
}
