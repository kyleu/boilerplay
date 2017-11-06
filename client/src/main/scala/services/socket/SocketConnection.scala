package services.socket

import models.RequestMessage
import services.entrypoint.Entrypoint
import services.event.{AuditEventHandler, EventHandler, LoggingEventHandler}
import util.{BinarySerializers, JsonSerializers, Logging}

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("SocketConnection")
class SocketConnection(val path: String, val binary: Boolean = false, debug: Boolean = false) extends Entrypoint("socket", debug) {
  private[this] val eventHandler = path match {
    case "/connect" => new LoggingEventHandler
    case "/admin/audit/activity/connect" => new AuditEventHandler
    case _ => throw new IllegalStateException(s"Unhandled path argument [$path].")
  }

  private[this] val socket = new NetworkSocket(eventHandler)

  NetworkMessage.register(sendMessage)
  Logging.debug(util.Config.projectName + " is connecting...")
  connect()

  protected def connect() = {
    val loc = org.scalajs.dom.document.location
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    val socketUrl = s"$wsProtocol://${loc.host}$path"
    val queryString = if (binary) { "?binary=true" } else { "" }
    socket.open(socketUrl + queryString)
  }

  def sendMessage(rm: RequestMessage): Unit = {
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
