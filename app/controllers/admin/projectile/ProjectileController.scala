package controllers.admin.projectile

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import io.circe.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@javax.inject.Singleton
class ProjectileController @javax.inject.Inject() (override val app: Application) extends AuthController("projectile") {
  def index() = withSession("projectileIndex", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(Json.obj(
      "timestamp" -> Json.fromLong(System.currentTimeMillis)
    )))
  }
}
