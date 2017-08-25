package services.socket

import org.scalajs.jquery.{jQuery => $}
import scribe.Logging
import services.NotificationService
import util.LoggingErrorHandler

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("SocketConnection")
class SocketConnection() extends Logging with NetworkHelper with ResponseMessageHelper {
  LoggingErrorHandler.install()
  NetworkMessage.register(sendMessage)
  logger.debug(util.Config.projectName + " is connecting...")
  connect()

  protected[this] def handleServerError(reason: String, content: String) = {
    val lp = $("#loading-panel")
    if (lp.css("display") == "block") {
      $("#tab-loading").text("Connection Error")
      val c = $("#loading-content", lp)
      c.text(s"Error loading database ($reason): $content")
    } else {
      NotificationService.error(reason, content)
    }
  }
}
