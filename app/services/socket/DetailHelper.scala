package services.socket

import models._

trait DetailHelper { this: SocketService =>
  protected[this] def handleSocketTrace() {
    val ret = SocketTraceResponse(id, user.id, user.username)
    sender() ! ret
  }

  protected[this] def handleClientTrace() {
    pendingDebugChannel = Some(sender())
    out ! SendTrace
  }

  protected[this] def handleDebugInfo(data: String) = pendingDebugChannel match {
    case Some(dc) =>
      val json = upickle.json.read(data)
      dc ! ClientTraceResponse(id, json)
    case None =>
      log.warn(s"Received unsolicited DebugInfo [$data] from [$id] with no active connection.")
  }
}
