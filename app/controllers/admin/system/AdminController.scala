package controllers.admin.system

import akka.util.Timeout
import controllers.BaseController
import models.Application
import models.InternalMessage.{GetSystemStatus, SystemStatus}

import scala.concurrent.Future

@javax.inject.Singleton
class AdminController @javax.inject.Inject() (override val app: Application) extends BaseController("admin") {
  import app.contexts.webContext

  def index = withSession("admin.index", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.index(request.identity)))
  }

  def explore = withSession("admin.explore", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.explore.explore(request.identity)))
  }

  def status = withSession("admin.status", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.status(request.identity)))
  }

  def dumpSockets = withSession("admin.sockets", admin = true) { implicit request => implicit td =>
    import akka.pattern.ask
    import scala.concurrent.duration._
    implicit val timeout: Timeout = Timeout(1.second)
    ask(app.supervisor, GetSystemStatus).mapTo[SystemStatus].map { x =>
      Ok(views.html.admin.sockets(request.identity, x.channels))
    }
  }
}
