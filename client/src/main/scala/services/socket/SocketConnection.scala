package services.socket

import models.RequestMessage
import services.entrypoint.Entrypoint
import services.event.EventHandler
import util.{BinarySerializers, JsonSerializers, Logging}

abstract class SocketConnection(
    key: String = "socket", val eventHandler: EventHandler, val binary: Boolean = false, debug: Boolean = false
) extends Entrypoint(key, debug) {
  protected[this] val socket = new NetworkSocket(eventHandler)

  def connect(path: String) = {
    NetworkMessage.register(sendMessage)
    Logging.debug(util.Config.projectName + " is connecting...")

    val url = {
      val loc = org.scalajs.dom.document.location
      val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
      val socketUrl = s"$wsProtocol://${loc.host}$path"
      val queryString = if (binary) { "?binary=true" } else { "" }
      socketUrl + queryString
    }
    socket.open(url)
  }

  private[this] def sendMessage(rm: RequestMessage): Unit = {
    if (socket.isConnected) {
      if (binary) {
        val x = BinarySerializers.writeRequestMessage(rm)
        socket.sendBinary(x)
      } else {
        val x = JsonSerializers.writeRequestMessage(rm, debug = debug)
        socket.sendString(x)
      }
      eventHandler.onRequestMessage(rm)
    } else {
      throw new IllegalStateException("Not connected.")
    }
  }
}
