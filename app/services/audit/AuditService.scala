package services.audit

import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import models.Configuration
import models.audit._
import play.api.inject.Injector
import com.kyleu.projectile.util.tracing.OpenTracingService
import com.kyleu.projectile.util.web.TracingWSClient

@javax.inject.Singleton
class AuditService @javax.inject.Inject() (
    tracing: OpenTracingService, inject: Injector, config: Configuration, ws: TracingWSClient, lookup: AuditLookup, val svc: AuditRowService
) extends Logging {
  AuditHelper.init(this)

  def callback(a: Audit, records: Seq[AuditRecordRow])(implicit trace: TraceData) = {
    if (records.exists(r => r.changes.as[Seq[AuditField]].right.get.nonEmpty)) {
      AuditNotifications.postToSlack(ws, config.slackConfig, a)
      AuditNotifications.persist(a, records)
      log.info(a.changeLog)
      a
    } else {
      log.info(s"Ignoring audit [${a.id}], as it has no changes.")
      a
    }
  }
}
