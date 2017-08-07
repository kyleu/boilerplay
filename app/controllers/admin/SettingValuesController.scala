package controllers.admin

import controllers.BaseController
import services.SettingValuesService
import util.Application
import util.FutureUtils.defaultContext

@javax.inject.Singleton
class SettingValuesController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def list(q: Option[String], limit: Option[Int], offset: Option[Int] = None) = {
    withAdminSession("settingValues.list") { implicit request =>
      val f = q match {
        case Some(query) if query.nonEmpty => SettingValuesService.search(query, None, limit.orElse(Some(100)), offset)
        case _ => SettingValuesService.getAll(None, limit.orElse(Some(100)), offset)
      }
      f.map { users =>
        Ok(views.html.admin.settingValues.listSettingValues(request.identity, q, users, limit.getOrElse(100), offset.getOrElse(0)))
      }
    }
  }

  def view(k: String) = withAdminSession("settingValues.view") { implicit request =>
    SettingValuesService.getById(k).map {
      case Some(model) => Ok(views.html.admin.settingValues.viewSettingValues(request.identity, model))
      case None => NotFound(s"No SettingValues found with k [$k].")
    }
  }
}
