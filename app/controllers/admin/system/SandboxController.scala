package controllers.admin.system

import akka.util.Timeout
import com.google.inject.Injector
import com.kyleu.projectile.models.config.{NavHtml, NavUrls, UiConfig, UserSettings}
import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.user.SystemUser

import scala.concurrent.ExecutionContext.Implicits.global
import models.sandbox.SandboxTask
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.GravatarUrl
import io.circe.JsonObject
import models.template.UserMenu
import util.Version

import scala.concurrent.Future
import scala.concurrent.duration._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (override val app: Application, injector: Injector) extends AuthController("sandbox") {
  implicit val timeout: Timeout = Timeout(10.seconds)

  def list = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.sandbox.sandboxList(request.identity, app.cfg(Some(request.identity), admin = true))))
  }

  private[this] val urls = NavUrls()

  def forUser(u: SystemUser, crumbs: String*) = {
    val menu = UserMenu.adminMenu(u)

    val settings = extract[JsonObject](u.settings)
    val theme = settings("theme").map(extract[String]).getOrElse("dark")
    val user = UserSettings(name = u.username, theme = theme, avatarUrl = Some(GravatarUrl(u.email)))

    val html = NavHtml(views.html.components.headerRightMenu(user.name, user.avatarUrl.getOrElse("")))
    val breadcrumbs = UserMenu.breadcrumbs(menu, crumbs)

    UiConfig(projectName = Version.projectName, menu = menu, urls = urls, html = html, user = user, breadcrumbs = breadcrumbs)
  }

  def scratchpad = withSession("scratchpad", admin = true) { implicit request => implicit td =>
    val cfg = forUser(request.identity, "a", "b", "c")

    Future.successful(Ok(views.html.admin.sandbox.scratchpad(request.identity, cfg)))
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
