package services.audit

import play.api.mvc.Call
import services.audit.AuditArgs._

object AuditRoutes {
  def getViewRoute(key: String, id: Seq[String]) = routeFor(key, getArg(id, _))

  private[this] def routeFor(key: String, arg: (Int) => String): Call = key.toLowerCase match {
    /* Start audit calls */
    /* End audit calls */

    case "user" => controllers.admin.user.routes.UserController.view(uuidArg(arg(0)))
    case _ => util.ise(s"Invalid model key [$key].")
  }
}
