package controllers.admin

import akka.util.Timeout
import controllers.BaseController
import models.sandbox.SandboxTask
import util.FutureUtils.defaultContext
import util.Application

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val app: Application) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def list = withAdminSession("sandbox.list") { implicit request =>
    Future.successful(Ok(views.html.admin.sandbox.sandboxList(request.identity)))
  }

  def run(key: String) = withAdminSession("sandbox." + key) { implicit request =>
    val sandbox = SandboxTask.withNameInsensitive(key)
    sandbox.run(app).map { result =>
      Ok(views.html.admin.sandbox.sandboxRun(request.identity, result))
    }
  }
}
