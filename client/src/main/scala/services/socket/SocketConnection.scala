package services.socket

import models.RequestMessage
import services.event.EventHandler
import util.{BinarySerializers, Logging}
import util.JsonSerializers._

abstract class SocketConnection(key: String, val handler: EventHandler, val binary: Boolean) {
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
        socket.sendBinary(BinarySerializers.writeRequestMessage(rm))
      } else {
        socket.sendString(rm.asJson.spaces2)
      }
      handler.onRequestMessage(rm)
    } else {
      throw new IllegalStateException("Not connected.")
    }
  }
}
