package services.socket

import org.scalajs.jquery.{jQuery => $}
import services.{InitService, Logging, NotificationService}

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("SocketConnection")
class SocketConnection() extends NetworkHelper with ResponseMessageHelper {
  InitService.initIfNeeded()
  NetworkMessage.register(sendMessage)
  Logging.debug(util.Config.projectName + " is connecting...")
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
