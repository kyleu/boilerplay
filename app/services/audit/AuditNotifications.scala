package services.audit

import models.audit.{Audit, AuditRecord}
import models.queries.audit.{AuditQueries, AuditRecordQueries}
import services.database.ApplicationDatabase
import util.FutureUtils.defaultContext
import util.{FutureUtils, Logging}
import util.tracing.TraceData
import util.web.TracingWSClient

import scala.concurrent.Future

object AuditNotifications extends Logging {
  def persist(a: Audit)(implicit trace: TraceData) = {
    log.debug(s"Persisting audit [${a.id}]...")
    val ret = ApplicationDatabase.executeF(AuditQueries.insert(a)).map { _ =>
      FutureUtils.acc(a.records, (r: AuditRecord) => ApplicationDatabase.executeF(AuditRecordQueries.insert(r)).map { _ =>
        log.debug(s"Persisted audit record [${r.id}] for audit [${a.id}].")
      })(FutureUtils.serviceContext).map { _ =>
        log.debug(s"Persisted audit [${a.id}] with [${a.records.size}] records.")
      }
    }
    ret.failed.foreach(x => log.warn(s"Unable to persist audit [${a.id}].", x))
    ret
  }

  def postToSlack(ws: TracingWSClient, config: SlackConfig, a: Audit)(implicit trace: TraceData) = if (config.enabled) {
    import util.JsonSerializers._

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
    ret
  } else {
    Future.successful("Skipped")
  }
}
