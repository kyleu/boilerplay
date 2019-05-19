package models.audit

import com.google.inject.Injector
import com.kyleu.projectile.models.audit.AuditCallbackProvider
import com.kyleu.projectile.models.auth.UserCredentials
import com.kyleu.projectile.models.result.data.DataFieldModel
import com.kyleu.projectile.services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import play.api.mvc.Call

import scala.concurrent.Future

class AuditCallbacks(override val injector: Injector) extends AuditCallbackProvider with Logging {
  override def getByPk(creds: UserCredentials, model: String, pk: String*)(implicit traceData: TraceData) = {
    getModel(creds, model, getArg(pk.toIndexedSeq, _))
  }

  override def getViewRoute(key: String, pk: IndexedSeq[String]) = routeFor(key, getArg(pk, _))

  private[this] def getModel(creds: UserCredentials, key: String, arg: Int => String)(implicit traceData: TraceData): Future[Option[DataFieldModel]] = {
    key.toLowerCase match {
      /* Start registry lookups */
      /* End registry lookups */
      case _ =>
        log.warn(s"Attempted to load invalid object type [$key:${arg(0)}].")
        Future.successful(None)
    }
  }

  private[this] def routeFor(key: String, arg: Int => String): Call = key.toLowerCase match {
    /* Start audit calls */
    /* End audit calls */

    case _ =>
      log.warn(s"Invalid model key [$key].")(TraceData.noop)
      controllers.routes.HomeController.home()
  }
}
