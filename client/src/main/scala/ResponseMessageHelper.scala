import models._
import services._
import utils.{Logging, NetworkMessage}

trait ResponseMessageHelper { this: Boilerplay =>
  protected[this] def handleMessage(rm: ResponseMessage) = rm match {

    case p: Pong => NetworkMessage.latencyMs = Some((System.currentTimeMillis - p.timestamp).toInt)

    case se: ServerError => handleServerError(se.reason, se.content)
    case _ => Logging.warn(s"Received unknown message of type [${rm.getClass.getSimpleName}].")
  }
}
