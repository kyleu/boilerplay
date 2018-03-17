package controllers.admin.rest

import controllers.BaseController
import models.Application
import services.rest.{RestRepository, RestRequestService}

import scala.concurrent.Future

@javax.inject.Singleton
class RestController @javax.inject.Inject() (override val app: Application, svc: RestRequestService) extends BaseController("rest") {
  import app.contexts.webContext

  def index() = withSession("index", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.rest.index(request.identity)))
  }

  def dump() = withSession("dump", admin = true) { implicit request => implicit td =>
    import io.circe.syntax._
    Future.successful(Ok(RestRepository.repo.asJson))
  }
}
