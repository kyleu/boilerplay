import org.scalajs.jquery.{jQuery => $}
import services.{InitService, NotificationService}

import scala.scalajs.js.annotation.JSExport

@JSExport
class Boilerplay extends NetworkHelper with ResponseMessageHelper {
  val debug = true

  InitService.init(sendMessage, connect)

  protected[this] def handleServerError(reason: String, content: String) = {
    val lp = $("#loading-panel")
    val isLoading = lp.css("display") == "block"
    if (isLoading) {
      $("#tab-loading").text("Connection Error")
      val c = $("#loading-content", lp)
      c.text(s"Error loading database ($reason): $content")
    } else {
      NotificationService.error(reason, content)
    }
  }
}
