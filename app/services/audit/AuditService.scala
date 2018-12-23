package services.audit

import models.audit._
import models.{Application, Configuration}
import play.api.inject.Injector
import util.Logging
import util.tracing.{TraceData, TracingService}
import util.web.TracingWSClient

@javax.inject.Singleton
class AuditService @javax.inject.Inject() (
    tracing: TracingService, inject: Injector, config: Configuration, ws: TracingWSClient, lookup: AuditLookup, val svc: AuditRowService
) extends Logging {
  lazy val supervisor = inject.instanceOf(classOf[Application]).supervisor
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
