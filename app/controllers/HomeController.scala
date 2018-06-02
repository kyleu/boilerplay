package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import models.Application

import scala.concurrent.Future

@javax.inject.Singleton
class HomeController @javax.inject.Inject() (override val app: Application) extends BaseController("home") {
  import app.contexts.webContext

  def home() = act("home") { implicit request => implicit td =>
    Future.successful(Ok(views.html.index(app.config.debug)))
  }

  def externalLink(url: String) = act("external.link") { implicit request => implicit td =>
    Future.successful(Redirect(if (url.startsWith("http")) { url } else { "http://" + url }))
  }

  def robots() = act("robots") { implicit request => implicit td =>
    Future.successful(Ok("User-agent: *\nDisallow: /"))
  }
}
