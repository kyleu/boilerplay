package controllers.admin.system

import akka.util.Timeout
import com.google.inject.Injector
import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.util.JsonSerializers._
import models.sandbox.SandboxTask

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (
    override val app: Application, injector: Injector
)(implicit ec: ExecutionContext) extends AuthController("sandbox") {
  implicit val timeout: Timeout = Timeout(10.seconds)

  def list = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.sandbox.sandboxList(request.identity, app.cfg(Some(request.identity), admin = true))))
  }

  def scratchpad = withSession("scratchpad", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.sandbox.scratchpad(request.identity, app.cfg(Some(request.identity), admin = true))))
  }

  def run(key: String, arg: Option[String]) = withSession(key, admin = true) { implicit request => implicit td =>
    val sandbox = SandboxTask.withNameInsensitive(key)
    sandbox.run(SandboxTask.Config(app.tracing, injector, arg)).map { result =>
      render {
        case Accepts.Html() => Ok(views.html.admin.sandbox.sandboxRun(request.identity, app.cfg(Some(request.identity), admin = true), result))
        case Accepts.Json() => Ok(result.asJson)
      }
    }
  }
}
