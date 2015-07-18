package controllers.admin

import java.util.UUID

import controllers.BaseController
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.audit.ClientTraceService
import services.user.AuthenticationEnvironment

@javax.inject.Singleton
class ClientTraceController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def traceList(q: String, sortBy: String, page: Int) = withAdminSession("list") { implicit request =>
    for {
      (count, traces) <- ClientTraceService.searchTraces(q, sortBy, page)
    } yield Ok(views.html.admin.clientTrace.traceList(q, sortBy, count, page, traces))
  }

  def traceDetail(id: UUID) = withAdminSession("detail") { implicit request =>
    ClientTraceService.getTrace(id).map {
      case Some(trace) => Ok(views.html.admin.clientTrace.traceDetail(trace))
      case None => NotFound(s"User [$id] not found.")
    }
  }

  def removeTrace(id: UUID) = withAdminSession("remove") { implicit request =>
    ClientTraceService.remove(id).map { ok =>
      Redirect(controllers.admin.routes.ClientTraceController.traceList(""))
    }
  }
}
