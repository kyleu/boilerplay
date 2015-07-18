package services.connection

import models._
import play.api.libs.json.{ JsObject, Json }
import services.audit.ClientTraceService
import utils.metrics.InstrumentedActor

trait ConnectionServiceTraceHelper extends InstrumentedActor { this: ConnectionService =>
  protected[this] def handleConnectionTrace() {
    val ret = TraceResponse(id, List(
      "userId" -> user.id,
      "name" -> user.username.getOrElse("Guest")
    ))
    sender() ! ret
  }

  protected[this] def handleClientTrace() {
    pendingDebugChannel = Some(sender())
    out ! SendDebugInfo
  }

  protected[this] def handleDebugInfo(data: String) = pendingDebugChannel match {
    case Some(dc) =>
      val json = Json.parse(data).as[JsObject]
      ClientTraceService.persistTrace(user.id, json)
      dc ! TraceResponse(id, json.fields)
    case None => log.warn(s"Received unsolicited DebugInfo [$data] from [$id].")
  }
}
