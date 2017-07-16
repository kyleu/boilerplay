package services.socket

import models._
import util.Config
import util.metrics.InstrumentedActor

trait RequestMessageHelper extends InstrumentedActor { this: SocketService =>
  override def receiveRequest = {
    case mr: MalformedRequest => timeReceive(mr) { log.error(s"MalformedRequest:  [${mr.reason}]: [${mr.content}].") }

    case p: Ping => timeReceive(p) { out ! Pong(p.timestamp) }
    case GetVersion => timeReceive(GetVersion) { out ! VersionResponse(Config.version) }

    case im: InternalMessage => handleInternalMessage(im)
    case rm: ResponseMessage => out ! rm
    case x => throw new IllegalArgumentException(s"Unhandled request message [${x.getClass.getSimpleName}].")
  }

  private[this] def handleInternalMessage(im: InternalMessage) = im match {
    case x => throw new IllegalArgumentException(s"Unhandled internal message [${x.getClass.getSimpleName}].")
  }
}
