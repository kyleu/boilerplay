package controllers.admin.system

import controllers.BaseController
import models.Application

@javax.inject.Singleton
class MetricsController @javax.inject.Inject() (override val app: Application) extends BaseController("metrics") {
  import app.contexts.webContext

  def showMetrics = withSession("admin.metrics", admin = true) { implicit request => implicit td =>
    val url = "http://localhost:2001/metrics?pretty=true"
    val call = app.ws.url("metrics", url).withHttpHeaders("Accept" -> JSON).get()
    call.map { json =>
      render {
        case Accepts.Html() => Ok(views.html.admin.metrics(request.identity, json.body))
        case Accepts.Json() => Ok(json.body).as(JSON)
      }
    }
  }
}
