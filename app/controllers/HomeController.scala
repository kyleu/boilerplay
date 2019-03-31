package controllers

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.services.database.ApplicationDatabase
import io.circe.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (override val app: Application) extends AuthController("home") {
  ApplicationDatabase.migrateSafe()

  def home() = withSession("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.index(request.identity, app.cfg(Some(request.identity), admin = false), app.config.debug)))
  }

  def externalLink(url: String) = withSession("external.link") { implicit request => implicit td =>
    Future.successful(Redirect(if (url.startsWith("http")) { url } else { "http://" + url }))
  }

  def ping(timestamp: Long) = withSession("ping") { implicit request => implicit td =>
    Future.successful(Ok(Json.obj("timestamp" -> Json.fromLong(timestamp))))
  }

  def robots() = withSession("robots") { implicit request => implicit td =>
    Future.successful(Ok("User-agent: *\nDisallow: /"))
  }
}
