package controllers.admin.ddl

import controllers.BaseController
import services.ddl.DdlService
import util.Application
import util.FutureUtils.defaultContext

@javax.inject.Singleton
class DdlController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def list(q: Option[String], limit: Option[Int], offset: Option[Int] = None) = {
    withAdminSession("ddl.list") { implicit request =>
      val f = q match {
        case Some(query) if query.nonEmpty => DdlService.search(query, None, limit.orElse(Some(100)), offset)
        case _ => DdlService.getAll(None, limit.orElse(Some(100)), offset)
      }
      f.map { users =>
        Ok(views.html.admin.ddl.ddl.listDdl(request.identity, q, users, limit.getOrElse(100), offset.getOrElse(0)))
      }
    }
  }

  def view(id: Int) = withAdminSession("ddl.view") { implicit request =>
    DdlService.getById(id).map {
      case Some(model) => Ok(views.html.admin.ddl.ddl.viewDdl(request.identity, model))
      case None => NotFound(s"No Ddl found with id [$id].")
    }
  }
}
