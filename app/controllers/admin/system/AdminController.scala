package controllers.admin.system

import controllers.BaseController
import models.Application
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (override val app: Application) extends BaseController("admin") {
  def index = withSession("admin.index", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.index(request.identity)))
  }

  def explore = withSession("admin.explore", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.explore.explore(request.identity)))
  }

  def status = withSession("admin.status", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.status(request.identity)))
  }
}
