package services.socket

import org.scalajs.dom.raw._
import services.event.EventHandler
import util.JsonSerializers

import scala.scalajs.js
import scala.scalajs.js.JSON

class NetworkSocket(handler: EventHandler) {
  private[this] var connecting = false
  private[this] var connected = false

  private[this] var ws: Option[WebSocket] = None

  def open(url: String) = if (connected) {
    throw new IllegalStateException("Already connected.")
  } else if (connecting) {
    throw new IllegalStateException("Already connecting.")
  } else {
    openSocket(url)
  }

  def send(s: String): Unit = ws match {
    case Some(socket) =>
      NetworkMessage.sentMessageCount += 1
      socket.send(s)
    case None => throw new IllegalStateException("No available socket connection.")
  }

  def send(c: String, v: js.Dynamic): Unit = send(s"""{"c": "$c", "v": ${JSON.stringify(v)} }""")

  def isConnected = connected

  private[this] def openSocket(url: String) = {
    connecting = true
    val socket = new WebSocket(url)
    socket.onopen = { (event: Event) => onConnectEvent(event) }
    socket.onerror = { (event: ErrorEvent) => onErrorEvent(event) }
    socket.onmessage = { (event: MessageEvent) => onMessageEvent(event) }
    socket.onclose = { (event: Event) => onCloseEvent(event) }
    ws = Some(socket)
  }

  private[this] def onConnectEvent(event: Event) = {
    connecting = false
    connected = true
    handler.onConnect()
    event
  }

  private[this] def onErrorEvent(event: ErrorEvent) = {
    handler.onError("Websocket error: " + event)
    event
  }

  private[this] def onMessageEvent(event: MessageEvent) = {
    val msg = event.data match {
      case s: String => JsonSerializers.readResponseMessage(s)
      case x => throw new IllegalStateException(s"Unhandled message data of type [${x.getClass}].")
    }
    NetworkMessage.receivedMessageCount += 1
    handler.onMessage(msg)
    event
  }

  private[this] def onCloseEvent(event: Event) = {
    connecting = false
    connected = false
    handler.onClose()
    event
  }
}
