package services.socket

import java.nio.ByteBuffer

import io.circe.parser.decode
import models.ResponseMessage
import org.scalajs.dom.raw._
import services.event.EventHandler
import util.ArrayBufferOps
import util.BinarySerializers

import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}

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

  def sendString(s: String): Unit = {
    val socket = ws.getOrElse(throw new IllegalStateException("No available socket connection."))
    NetworkMessage.sentMessageCount += 1
    NetworkMessage.sentBytes += s.getBytes.length
    socket.send(s)
  }

  def sendBinary(data: Array[Byte]): Unit = {
    val socket = ws.getOrElse(throw new IllegalStateException("No available socket connection."))
    NetworkMessage.sentMessageCount += 1
    NetworkMessage.sentBytes += data.length
    socket.send(ArrayBufferOps.convertBuffer(ByteBuffer.wrap(data)))
  }

  def isConnected = connected

  private[this] def openSocket(url: String) = {
    connecting = true
    val socket = new WebSocket(url)
    socket.onopen = { (event: Event) => onConnectEvent(event) }
    socket.onerror = { (event: Event) => onErrorEvent(event) }
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

  private[this] def onErrorEvent(event: Event) = {
    handler.onError(event)
    event
  }

  private[this] def process(msg: ResponseMessage) = {
    NetworkMessage.receivedMessageCount += 1
    handler.onResponseMessage(msg)
  }

  private[this] def onMessageEvent(event: MessageEvent): Unit = event.data match {
    case s: String =>
      NetworkMessage.receivedBytes += s.getBytes.length
      process(decode[ResponseMessage](s) match {
        case Right(x) => x
        case Left(err) => throw err
      })
    case b: Blob =>
      val reader = new FileReader()
      def onLoadEnd(ev: ProgressEvent) = {
        val buff = reader.result
        val ab = buff.asInstanceOf[ArrayBuffer]
        val data = TypedArrayBuffer.wrap(ab)
        NetworkMessage.receivedBytes += ab.byteLength
        val msg = BinarySerializers.readResponseMessage(data)
        process(msg)
      }
      reader.onloadend = onLoadEnd _
      reader.readAsArrayBuffer(b)
    case buff: ArrayBuffer =>
      val data = TypedArrayBuffer.wrap(buff)
      process(BinarySerializers.readResponseMessage(data))
    case x => throw new IllegalStateException(s"Unhandled message data of type [$x].")
  }

  private[this] def onCloseEvent(event: Event) = {
    connecting = false
    connected = false
    handler.onClose()
    event
  }
}
