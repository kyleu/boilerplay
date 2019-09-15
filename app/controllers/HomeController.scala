package controllers

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.module.Application

import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (override val app: Application)(implicit ec: ExecutionContext) extends AuthController("home") {
  def home() = withSession("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.index(app.cfg(Some(request.identity)), app.config.debug)))
  }
}
