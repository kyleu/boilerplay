package services.event

import models.{RequestMessage, ResponseMessage}
import org.scalajs.jquery.{jQuery => $}
import services.socket.NetworkMessage
import util.Logging
import org.scalajs.dom.raw.Event

class LoggingEventHandler() extends EventHandler {
  private[this] def getConnEl(key: String) = {
    val el = $(s"#socket-connection-$key")
    if (el.length == 1) { Some(el) } else { None }
  }

  val formEl = getConnEl("form")
  val tableEl = getConnEl("table")

  val statusEl = getConnEl("status")
  val latencyEl = getConnEl("latency")
  val sentMessagesEl = getConnEl("sent-messages")
  val sentBytesEl = getConnEl("sent-bytes")
  val receivedMessagesEl = getConnEl("received-messages")
  val receivedBytesEl = getConnEl("received-bytes")

  override def onConnect(): Unit = {
    Logging.info("Socket connected.")
    statusEl.foreach(_.text("Connected"))
    formEl.foreach(_.hide())
    tableEl.foreach(_.show())
    super.onConnect()
  }

  override def onRequestMessage(rm: RequestMessage): Unit = {
    super.onRequestMessage(rm)
    sentMessagesEl.foreach(_.text(util.NumberUtils.withCommas(NetworkMessage.sentMessageCount)))
    sentBytesEl.foreach(_.text(util.NumberUtils.withCommas(NetworkMessage.sentBytes)))
  }

  override def onResponseMessage(msg: ResponseMessage): Unit = {
    super.onResponseMessage(msg)
    receivedMessagesEl.foreach(_.text(util.NumberUtils.withCommas(NetworkMessage.receivedMessageCount)))
    receivedBytesEl.foreach(_.text(util.NumberUtils.withCommas(NetworkMessage.receivedBytes)))
  }

  override protected def onLatency(ms: Int): Unit = {
    latencyEl.foreach(_.text(ms + "ms"))
    super.onLatency(ms)
  }

  override def onError(err: Event): Unit = super.onError(err)
  override def onClose(): Unit = super.onClose()
}
