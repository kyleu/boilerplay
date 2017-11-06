package services.socket

import services.event.LoggingEventHandler

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("TestSocketConnection")
class TestSocketConnection(binary: Boolean = true, debug: Boolean = false) extends SocketConnection("test", new LoggingEventHandler, binary, debug) {
  connect("/connect")
}
