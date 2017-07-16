package util

import akka.actor.ActorRef
import models.ServerError

object ExceptionUtils extends Logging {
  def actorErrorFunction(out: ActorRef, key: String, t: Throwable) = {
    log.warn(s"Error [$key] encountered of type [${t.getClass.getSimpleName}].", t)
    out ! ServerError(key, t.getMessage)
  }
}
