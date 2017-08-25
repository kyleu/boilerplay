package services.socket

import models.RequestMessage

object NetworkMessage {
  var latencyMs: Option[Int] = None
  var sentMessageCount = 0
  var receivedMessageCount = 0

  private[this] var sendF: Option[(RequestMessage) => Unit] = None

  def register(f: (RequestMessage) => Unit) = sendF match {
    case Some(_) => throw new IllegalStateException("Double registration.")
    case None => sendF = Some(f)
  }

  def sendMessage(requestMessage: RequestMessage) = sendF match {
    case Some(f) => f(requestMessage)
    case None => throw new IllegalStateException("Message send before start.")
  }
}
