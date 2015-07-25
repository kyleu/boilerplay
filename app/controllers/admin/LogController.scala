package controllers.admin

import controllers.BaseController
import play.api.i18n.MessagesApi
import services.audit.LogService
import services.user.AuthenticationEnvironment

import scala.concurrent.Future

@javax.inject.Singleton
class LogController @javax.inject.Inject() (override val messagesApi: MessagesApi, override val env: AuthenticationEnvironment) extends BaseController {
  def list() = withAdminSession("log.list") { implicit request =>
    val files = LogService.listFiles()
    Future.successful(Ok(views.html.admin.log.logList(files)))
  }

  def view(name: String) = withAdminSession("log.view") { implicit request =>
    val logs = LogService.getLogs(name)
    Future.successful(Ok(views.html.admin.log.logView(name, logs)))
  }
}
