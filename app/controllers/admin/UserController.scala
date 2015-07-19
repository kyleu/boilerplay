package controllers.admin

import java.util.UUID

import controllers.BaseController
import models.database.queries.auth.UserQueries
import models.database.queries.report.ReportQueries
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import services.history.RequestHistoryService
import services.user.AuthenticationEnvironment

import scala.concurrent.Future

@javax.inject.Singleton
class UserController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def userList(q: String, sortBy: String, page: Int) = withAdminSession("list") { implicit request =>
    for {
      count <- Database.query(UserQueries.searchCount(q))
      users <- Database.query(UserQueries.search(q, getOrderClause(sortBy), Some(page)))
      requestCounts <- Database.query(new ReportQueries.RequestCountForUsers(users.map(_.id)))
    } yield Ok(views.html.admin.user.userList(q, sortBy, count, page, users, requestCounts))
  }

  def userDetail(id: UUID) = withAdminSession("detail") { implicit request =>
    env.identityService.retrieve(id).flatMap {
      case Some(user) => for {
        requestCount <- RequestHistoryService.getCountByUser(id)
      } yield Ok(views.html.admin.user.userDetail(user, requestCount))
      case None => Future.successful(NotFound(s"User [$id] not found."))
    }
  }

  def removeUser(id: UUID) = withAdminSession("remove") { implicit request =>
    env.userService.remove(id).map { result =>
      val success = if (result("users") == 1) { "successfully" } else { "with an error" }
      val profiles = result("profiles")
      val requests = result("requests")
      val cards = result("cards")
      val moves = result("moves")
      val timing = result("timing")
      val msg = s"User [$id] removed $success in [${timing}ms]. " +
        s"Removed $profiles profiles, and $requests requests."
      Redirect(controllers.admin.routes.UserController.userList("")).flashing("success" -> msg)
    }
  }

  private[this] def getOrderClause(orderBy: String) = orderBy match {
    case "created" => "created desc"
    case x => x
  }
}
