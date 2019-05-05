package services.audit

import play.api.mvc.Call
import com.kyleu.projectile.services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData

object AuditRoutes extends Logging {
  def getViewRoute(key: String, id: IndexedSeq[String]) = routeFor(key, getArg(id, _))

  private[this] def routeFor(key: String, arg: Int => String): Call = key.toLowerCase match {
    /* Start audit calls */
    /* Projectile export section [boilerplay] */
    case "scheduledtaskrunrow" => controllers.admin.task.routes.ScheduledTaskRunRowController.view(uuidArg(arg(0)))
    case "syncprogressrow" => controllers.admin.sync.routes.SyncProgressRowController.view(stringArg(arg(0)))
    /* End audit calls */

    case _ =>
      log.warn(s"Invalid model key [$key].")(TraceData.noop)
      controllers.admin.system.routes.AdminController.explore()
  }
}
