package services.audit

import models.audit.Audit
import models.queries.audit.{AuditQueries, AuditRecordQueries}
import services.database.SystemDatabase
import util.FutureUtils.defaultContext
import util.Logging
import util.tracing.TraceData
import util.web.TracingWSClient

object AuditNotifications extends Logging {
  def persist(a: Audit)(implicit trace: TraceData) = {
    log.info(s"Persisting audit [${a.id}]...")
    SystemDatabase.execute(AuditQueries.insert(a))
    log.info(s"Persisted audit [${a.id}].")
    a.records.foreach { r =>
      SystemDatabase.execute(AuditRecordQueries.insert(r))
      log.info(s"Persisted audit record [${r.id}] for audit [${a.id}].")
    }
  }

  def postToSlack(ws: TracingWSClient, config: SlackConfig, a: Audit)(implicit trace: TraceData) = if (config.enabled) {
    import io.circe.syntax._

    val body = Map(
      "channel" -> config.channel,
      "username" -> config.username,
      "icon_url" -> config.iconUrl,
      "text" -> a.changeLog
    )

    val call = ws.url("slack.post", config.url).withHttpHeaders("Accept" -> "application/json").post(body.asJson.spaces2)
    val ret = call.map { x =>
      if (x.status == 200) {
        "OK"
      } else {
        log.warn("Unable to post to Slack (" + x.status + "): [" + x.body + "].")
        "ERROR"
      }
    }
    ret.failed.foreach(x => log.warn("Unable to post to Slack.", x))
  }

}
