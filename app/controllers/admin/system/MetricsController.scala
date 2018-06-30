package controllers.admin.system

import controllers.BaseController
import models.Application
import util.metrics.Instrumented

import scala.concurrent.Future

@javax.inject.Singleton
class MetricsController @javax.inject.Inject() (override val app: Application) extends BaseController("metrics") {
  import app.contexts.webContext

  def showMetrics = withSession("admin.metrics", admin = true) { implicit request => implicit td =>
    Future.successful(render {
      case Accepts.Html() => Ok(views.html.admin.metrics(request.identity, Instrumented.reg.scrape()))
      case Accepts.Json() => Ok(Instrumented.reg.scrape()).as(JSON)
    })
  }
}
