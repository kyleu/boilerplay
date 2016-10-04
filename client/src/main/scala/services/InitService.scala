package services

import java.net.URLDecoder
import java.util.UUID

import models.RequestMessage
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $}
import ui._
import utils.{Logging, NetworkMessage, TemplateUtils}

import scala.scalajs.js

object InitService {
  def init(sendMessage: (RequestMessage) => Unit, connect: () => Unit) {
    Logging.installErrorHandler()
    NetworkMessage.register(sendMessage)

    ShortcutService.init()
    Logging.debug("Database Flow has started.")
    connect()
  }
}
