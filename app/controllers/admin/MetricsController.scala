package controllers.admin

import controllers.BaseController
import utils.ApplicationContext
import play.api.libs.concurrent.Execution.Implicits.defaultContext

@javax.inject.Singleton
class MetricsController @javax.inject.Inject() (override val ctx: ApplicationContext) extends BaseController {
  def showMetrics = withAdminSession("admin-metrics") { implicit request =>
    val url = "http://localhost:2001/metrics?pretty=true"
    val call = ctx.ws.url(url).withHeaders("Accept" -> "application/json")
    val f = call.get()

    f.map { json =>
      Ok(views.html.admin.metrics(request.identity, json.body))
    }
  }
}
