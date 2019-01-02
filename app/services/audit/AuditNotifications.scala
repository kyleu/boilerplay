package services.audit

import com.kyleu.projectile.services.database.ApplicationDatabase
import models.audit.{Audit, AuditRecordRow}
import scala.concurrent.ExecutionContext.Implicits.global
import models.queries.audit.{AuditQueries, AuditRecordRowQueries}
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import com.kyleu.projectile.web.util.TracingWSClient

import scala.concurrent.{ExecutionContext, Future}

object AuditNotifications extends Logging {
  private[this] def acc[T, U](seq: Seq[T], f: T => Future[U])(ec: ExecutionContext) = seq.foldLeft(Future.successful(Seq.empty[U])) { (ret, i) =>
    ret.flatMap(s => f(i).map(_ +: s)(ec))(ec)
  }

  def persist(a: Audit, records: Seq[AuditRecordRow])(implicit trace: TraceData) = {
    log.debug(s"Persisting audit [${a.id}]...")
    val ret = ApplicationDatabase.executeF(AuditQueries.insert(a)).map { _ =>
      acc(records, (r: AuditRecordRow) => ApplicationDatabase.executeF(AuditRecordRowQueries.insert(r)).map { _ =>
        log.debug(s"Persisted audit record [${r.id}] for audit [${a.id}].")
      })(global).map { _ =>
        log.debug(s"Persisted audit [${a.id}] with [${records.size}] records.")
      }
    }
    ret.failed.foreach(x => log.warn(s"Unable to persist audit [${a.id}].", x))
    ret
  }
  def postToSlack(ws: TracingWSClient, config: SlackConfig, a: Audit)(implicit trace: TraceData) = if (config.enabled) {
    import com.kyleu.projectile.util.JsonSerializers._

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
