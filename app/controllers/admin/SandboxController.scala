package controllers.admin

import akka.util.Timeout
import controllers.BaseController
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.sandbox.SandboxTask
import utils.ApplicationContext

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def list = withAdminSession("sandbox.list") { implicit request =>
    Future.successful(Ok(views.html.admin.sandbox.list(request.identity)))
  }

  def sandbox(key: String) = withAdminSession("sandbox." + key) { implicit request =>
    val sandbox = SandboxTask.withName(key)
    sandbox.run(ctx).map { result =>
      Ok(views.html.admin.sandbox.view(request.identity, sandbox, result))
    }
  }
}
