package services

import models.RequestMessage
import util.{Logging, NetworkMessage}

object InitService {
  def init(sendMessage: (RequestMessage) => Unit, connect: () => Unit) = {
    Logging.installErrorHandler()
    NetworkMessage.register(sendMessage)

    Logging.debug(util.Config.projectName + " has started.")
    connect()
  }
}
