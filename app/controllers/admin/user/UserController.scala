package controllers.admin.user

import controllers.BaseController
import services.user.UserService
import util.Application
import util.FutureUtils.defaultContext

@javax.inject.Singleton
class UserController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def list(q: Option[String], limit: Option[Int], offset: Option[Int] = None) = {
    withAdminSession("user.list") { implicit request =>
      val f = q match {
        case Some(query) if query.nonEmpty => UserService.search(query, None, limit.orElse(Some(100)), offset)
        case _ => UserService.getAll(None, limit.orElse(Some(100)), offset)
      }
      f.map { users =>
        Ok(views.html.admin.user.user.listUser(request.identity, q, users, limit.getOrElse(100), offset.getOrElse(0)))
      }
    }
  }

  def view(id: java.util.UUID) = withAdminSession("user.view") { implicit request =>
    UserService.getById(id).map {
      case Some(model) => Ok(views.html.admin.user.user.viewUser(request.identity, model))
      case None => NotFound(s"No User found with id [$id].")
    }
  }
}
