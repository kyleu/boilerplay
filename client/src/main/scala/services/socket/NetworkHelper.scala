package services.socket

import models.{Ping, RequestMessage}
import services.Logging
import util.{DateUtils, JsonSerializers}

import scala.scalajs.js.timers._

trait NetworkHelper { this: SocketConnection =>
  private[this] val socket = new NetworkSocket(onSocketConnect _, onSocketMessage, onSocketError, onSocketClose _)

  protected[this] var latencyMs: Option[Int] = None

  protected def connect() = {
    val loc = org.scalajs.dom.document.location
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    val socketUrl = s"$wsProtocol://${loc.host}/connect"
    socket.open(socketUrl)
  }

  private def sendPing(): Unit = {
    if (socket.isConnected) {
      NetworkMessage.sendMessage(Ping(DateUtils.now))
    }
    setTimeout(10000)(sendPing())
  }

  setTimeout(1000)(sendPing())

  protected[this] def onSocketConnect(): Unit = {
    Logging.debug("Socket connected.")
  }

  protected[this] def onSocketError(error: String): Unit = {
    Logging.error(s"Socket error [$error].")
  }

  protected[this] def onSocketClose(): Unit = {
    /*
    val callback = () => {
      Logging.info("Attempting to reconnect Websocket.")
      socket.open(NavigationService.socketUrl)
    }
    ReconnectManager.show(callback, NotificationService.getLastError match {
      case Some(e) => s"${e._1}: ${e._2}"
      case None => "The connection to the server was closed."
    })
    */
  }

  def sendMessage(rm: RequestMessage): Unit = {
    if (socket.isConnected) {
      val json = JsonSerializers.writeRequestMessage(rm, debug = true)
      socket.send(json)
    } else {
      throw new IllegalStateException("Not connected.")
    }
  }

  protected[this] def onSocketMessage(json: String): Unit = {
    val msg = JsonSerializers.readResponseMessage(json)
    handleMessage(msg)
  }
}
