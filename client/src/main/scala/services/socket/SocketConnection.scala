package services.socket

import models.RequestMessage
import services.event.EventHandler
import util.{BinarySerializers, JsonSerializers, Logging}

abstract class SocketConnection(
    key: String = "socket", val handler: EventHandler, debug: Boolean = false, val binary: Boolean = false
) {
  Logging.init(debug)
  protected[this] val socket = new NetworkSocket(handler)

  def connect(path: String) = {
    NetworkMessage.register(sendMessage)
    Logging.debug(util.Config.projectName + " is connecting...")

    val url = websocketUrl(path)
    Logging.info(s"Socket [$key] starting with url [$url].")
    socket.open(url)
  }

  private[this] def websocketUrl(path: String) = {
    val loc = org.scalajs.dom.document.location
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    val socketUrl = s"$wsProtocol://${loc.host}$path"
    val queryString = if (binary) { "?binary=true" } else { "" }
    socketUrl + queryString
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
      handler.onRequestMessage(rm)
    } else {
      throw new IllegalStateException("Not connected.")
    }
  }
}
