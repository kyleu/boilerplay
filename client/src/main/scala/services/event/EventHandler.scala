package services.event

import models.{RequestMessage, ResponseMessage}
import models.ResponseMessage.{Pong, ServerError, UserSettings}
import services.notification.NotificationService
import services.socket.NetworkMessage
import services.ui.UserManager
import util.{DateUtils, Logging}

import scala.scalajs.js.timers.setTimeout
import org.scalajs.dom.raw.Event

trait EventHandler {
  private[this] def sendPing(): Unit = {
    NetworkMessage.sendMessage(models.RequestMessage.Ping(DateUtils.now))
    setTimeout(10000)(sendPing())
  }

  def onConnect(): Unit = {
    Logging.debug("Socket connected.")
    setTimeout(1000)(sendPing())
  }

  protected def onLatency(ms: Int): Unit = {
    NetworkMessage.latencyMs = Some(ms)
  }

  def onRequestMessage(rm: RequestMessage): Unit = {}

  def onResponseMessage(msg: ResponseMessage): Unit = msg match {
    case p: Pong => onLatency((System.currentTimeMillis - DateUtils.toMillis(p.ts)).toInt)
    case us: UserSettings => UserManager.onUserSettings(us)
    case se: ServerError => NotificationService.error(se.reason, se.content)
    case _ => Logging.warn(s"Received unknown message of type [${msg.getClass.getSimpleName}].")
  }

  def onError(err: Event): Unit = {
    Logging.error(s"Socket error")
    Logging.logJs(err)
  }

  def onClose(): Unit = {
    Logging.info("Socket closed.")
  }
}
