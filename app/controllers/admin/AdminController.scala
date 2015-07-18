package controllers.admin

import akka.pattern.ask
import akka.util.Timeout
import controllers.BaseController
import models._
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Schema
import services.scheduled.ScheduledTask
import services.supervisor.ActorSupervisor
import services.user.{ UserService, AuthenticationEnvironment }

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment,
    scheduledTask: ScheduledTask
) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def index = withAdminSession("index") { implicit request =>
    Future.successful(Ok(views.html.admin.index()))
  }

  def enable = withSession("admin.enable") { implicit request =>
    UserService.enableAdmin(request.identity).map { response =>
      Ok(response)
    }
  }

  def status = withAdminSession("status") { implicit request =>
    (ActorSupervisor.instance ask GetSystemStatus).map {
      case x: SystemStatus => Ok(views.html.admin.status(x))
    }
  }

  def wipeSchema(key: String) = withAdminSession("wipe.schema") { implicit request =>
    if (key == "SuperSecretPassword") {
      Schema.wipe().map { tables =>
        Ok(s"OK, Wiped tables [${tables.mkString(", ")}].")
      }
    } else {
      Future.successful(NotFound("Missing key."))
    }
  }
}
