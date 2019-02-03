package controllers.admin.system

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (override val app: Application, authActions: AuthActions) extends AuthController("admin") {
  def index = withSession("admin.index", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.index(request.identity, authActions)))
  }

  def explore = withSession("admin.explore", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.explore.explore(request.identity, authActions)))
  }

  def status = withSession("admin.status", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.status(request.identity, authActions)))
  }
}
