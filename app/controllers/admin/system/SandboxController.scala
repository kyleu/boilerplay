package controllers.admin.system

import akka.util.Timeout
import controllers.BaseController
import models.Application
import models.sandbox.SandboxTask

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val app: Application) extends BaseController("sandbox") {
  import app.contexts.webContext

  implicit val timeout = Timeout(10.seconds)

  def list = withSession("sandbox.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.sandbox.sandboxList(request.identity)))
  }

  def run(key: String, arg: Option[String]) = withSession("sandbox." + key, admin = true) { implicit request => implicit td =>
    val sandbox = SandboxTask.withNameInsensitive(key)
    sandbox.run(app, arg).map { result =>
      Ok(views.html.admin.sandbox.sandboxRun(request.identity, result))
    }
  }
}
