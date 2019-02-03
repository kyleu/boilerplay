package controllers.admin.system

import akka.util.Timeout
import com.google.inject.Injector
import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions

import scala.concurrent.ExecutionContext.Implicits.global
import models.sandbox.SandboxTask
import com.kyleu.projectile.util.JsonSerializers._

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val app: Application, authActions: AuthActions, injector: Injector) extends AuthController("sandbox") {
  implicit val timeout: Timeout = Timeout(10.seconds)

  def list = withSession("sandbox.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.sandbox.sandboxList(request.identity, authActions)))
  }

  def run(key: String, arg: Option[String]) = withSession("sandbox." + key, admin = true) { implicit request => implicit td =>
    val sandbox = SandboxTask.withNameInsensitive(key)
    sandbox.run(SandboxTask.Config(app.tracing, injector, arg)).map { result =>
      render {
        case Accepts.Html() => Ok(views.html.admin.sandbox.sandboxRun(request.identity, authActions, result))
        case Accepts.Json() => Ok(result.asJson)
      }
    }
  }
}
