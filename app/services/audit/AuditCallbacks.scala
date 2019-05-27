package models.audit

import com.google.inject.Injector
import com.kyleu.projectile.models.audit.AuditCallbackProvider
import com.kyleu.projectile.services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import play.api.mvc.Call

class AuditCallbacks(override val injector: Injector) extends AuditCallbackProvider with Logging {
  override def getViewRoute(key: String, pk: IndexedSeq[String]) = routeFor(key, getArg(pk, _))

  private[this] def routeFor(key: String, arg: Int => String): Call = key.toLowerCase match {
    /* Start audit calls */
    /* End audit calls */

    case _ =>
      log.warn(s"Invalid model key [$key].")(TraceData.noop)
      controllers.routes.HomeController.home()
  }
}
