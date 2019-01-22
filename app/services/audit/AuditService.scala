package services.audit

import com.kyleu.projectile.models.Configuration
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import models.audit._
import play.api.inject.Injector
import com.kyleu.projectile.util.tracing.OpenTracingService

@javax.inject.Singleton
class AuditService @javax.inject.Inject() (
    tracing: OpenTracingService, inject: Injector, config: Configuration, lookup: AuditLookup, val svc: AuditRowService
) extends Logging {
  AuditHelper.init(this)

  def callback(a: Audit, records: Seq[AuditRecordRow])(implicit trace: TraceData) = {
    if (records.exists(r => r.changes.as[Seq[AuditField]].right.get.nonEmpty)) {
      AuditNotifications.persist(a, records)
      log.info(a.changeLog)
      a
    } else {
      log.info(s"Ignoring audit [${a.id}], as it has no changes.")
      a
    }
  }
}
