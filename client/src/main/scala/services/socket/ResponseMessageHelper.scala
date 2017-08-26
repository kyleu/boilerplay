package services.socket

import models._
import services.Logging
import services.ui.UserManager
import util.DateUtils

trait ResponseMessageHelper { this: SocketConnection =>
  protected[this] def handleMessage(rm: ResponseMessage) = rm match {
    case p: Pong => NetworkMessage.latencyMs = Some((System.currentTimeMillis - DateUtils.toMillis(p.timestamp)).toInt)
    case us: UserSettings => UserManager.onUserSettings(us)

    case se: ServerError => handleServerError(se.reason, se.content)
    case _ => Logging.warn(s"Received unknown message of type [${rm.getClass.getSimpleName}].")
  }
}
