package controllers.admin.user

import controllers.BaseController
import services.user.PasswordInfoService
import util.Application
import util.FutureUtils.defaultContext

@javax.inject.Singleton
class PasswordInfoController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def list(q: Option[String], limit: Option[Int], offset: Option[Int] = None) = {
    withAdminSession("passwordInfo.list") { implicit request =>
      val f = q match {
        case Some(query) if query.nonEmpty => PasswordInfoService.search(query, None, limit.orElse(Some(100)), offset)
        case _ => PasswordInfoService.getAll(None, limit.orElse(Some(100)), offset)
      }
      f.map { users =>
        Ok(views.html.admin.user.passwordInfo.listPasswordInfo(request.identity, q, users, limit.getOrElse(100), offset.getOrElse(0)))
      }
    }
  }

  def view(provider: String, key: String) = withAdminSession("passwordInfo.view") { implicit request =>
    PasswordInfoService.getById(provider, key).map {
      case Some(model) => Ok(views.html.admin.user.passwordInfo.viewPasswordInfo(request.identity, model))
      case None => NotFound(s"No PasswordInfo found with provider, key [$provider, $key].")
    }
  }
}
