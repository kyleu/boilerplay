package controllers.admin.rest

import controllers.BaseController
import models.Application

import scala.concurrent.Future
import scala.io.Source
import scala.util.control.NonFatal

@javax.inject.Singleton
class OpenApiController @javax.inject.Inject() (override val app: Application) extends BaseController("rest") {
  import app.contexts.webContext

  private[this] lazy val yamlContent = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("openapi.yaml")).mkString

  def yaml() = withSession("json", admin = true) { implicit request => implicit td =>
    try {
      Future.successful(Ok(yamlContent).as("text/x-yaml"))
    } catch {
      case NonFatal(_) => Future.successful(InternalServerError("Unable to load [openapi.yaml]."))
    }
  }

  def ui() = withSession("index", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.rest.swagger(request.identity)))
  }
}
