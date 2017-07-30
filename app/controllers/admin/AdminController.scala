package controllers.admin

import controllers.BaseController
import util.Application

import scala.concurrent.Future

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def index = withAdminSession("admin.index") { implicit request =>
    Future.successful(Ok(views.html.admin.index(request.identity)))
  }

  def explore = withAdminSession("admin.explore") { implicit request =>
    Future.successful(Ok(views.html.admin.explore(request.identity)))
  }

  def status = withAdminSession("admin.status") { implicit request =>
    Future.successful(Ok(views.html.admin.status(request.identity)))
  }
}
