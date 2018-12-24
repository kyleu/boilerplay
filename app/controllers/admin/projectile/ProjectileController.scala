package controllers.admin.projectile

import controllers.BaseController
import io.circe.Json
import models.Application

import scala.concurrent.Future

@javax.inject.Singleton
class ProjectileController @javax.inject.Inject() (override val app: Application) extends BaseController("projectile") {
  import app.contexts.webContext

  def index() = withSession("projectileIndex", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(Json.obj(
      "timestamp" -> Json.fromLong(System.currentTimeMillis)
    )))
  }
}