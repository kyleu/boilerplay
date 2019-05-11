package services.audit

import com.google.inject.Injector
import com.kyleu.projectile.models.audit.AuditCallbackProvider
import com.kyleu.projectile.models.auth.UserCredentials
import com.kyleu.projectile.util.tracing.TraceData

class AuditCallbacks(override val injector: Injector, auditLookup: AuditLookup) extends AuditCallbackProvider {
  override def getByPk(creds: UserCredentials, model: String, pk: String*)(implicit td: TraceData) = {
    auditLookup.getByPk(creds, model, pk: _*)
  }
  override def getViewRoute(key: String, pk: IndexedSeq[String]) = AuditRoutes.getViewRoute(key, pk)
}
