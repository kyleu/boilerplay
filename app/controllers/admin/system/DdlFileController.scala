package controllers.admin.system

import controllers.BaseController
import io.circe.syntax._
import models.Application
import models.ddl.DdlQueries
import services.database.ApplicationDatabase
import util.FutureUtils.defaultContext

import scala.concurrent.Future

@javax.inject.Singleton
class DdlFileController @javax.inject.Inject() (override val app: Application) extends BaseController("ddlFile") {
  def list = withSession("list", admin = true) { implicit request => implicit td =>
    val r = ApplicationDatabase.query(DdlQueries.getAll())
    Future.successful(render {
      case Accepts.Html() => Ok(views.html.admin.ddlFileList(request.identity, r))
      case Accepts.Json() => Ok(r.asJson.spaces2).as(JSON)
    })
  }
}
