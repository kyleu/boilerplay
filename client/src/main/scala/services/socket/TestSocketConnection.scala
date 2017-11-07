package services.socket

import services.event.LoggingEventHandler

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("TestSocketConnection")
class TestSocketConnection(binary: Boolean = true, debug: Boolean = false) extends SocketConnection("test", new LoggingEventHandler, binary, debug) {
  connect("/connect")

  @JSExport
  def flood(): Unit = {
    val ts = util.DateUtils.now
    import scala.scalajs.js.timers.setTimeout
    (0 until 10).foreach(_ => NetworkMessage.sendMessage(models.Ping(ts)))
    setTimeout(100)(flood())
  }
}
