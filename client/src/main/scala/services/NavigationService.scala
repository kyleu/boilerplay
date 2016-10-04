package services

import java.util.UUID

import utils.Logging

import scala.scalajs.js
import scala.util.control.NonFatal

object NavigationService {
  private[this] lazy val loc = org.scalajs.dom.document.location

  lazy val connectionId = {
    val s = js.Dynamic.global.connectionId.toString
    if (s.length != 36) {
      throw new IllegalStateException(s"Missing connection ID. Encountered [$s], of length [${s.length}].")
    }
    UUID.fromString(s)
  }

  lazy val socketUrl = {
    val wsProtocol = if (loc.protocol == "https:") { "wss" } else { "ws" }
    s"$wsProtocol://${loc.host}/q/$connectionId/websocket"
  }
}
