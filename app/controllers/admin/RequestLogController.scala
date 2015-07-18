package controllers.admin

import controllers.BaseController
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.history.RequestHistoryService
import services.user.AuthenticationEnvironment

@javax.inject.Singleton
class RequestLogController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def requestList(q: String, sortBy: String, page: Int) = withAdminSession("list") { implicit request =>
    RequestHistoryService.searchRequests(q, getOrderClause(sortBy), page).map { result =>
      Ok(views.html.admin.request.requestList(q, sortBy, result._1, page, result._2))
    }
  }

  private[this] def getOrderClause(orderBy: String) = orderBy match {
    case "occurred" => "occurred desc"
    case x => x
  }
}
