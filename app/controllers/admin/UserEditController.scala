package controllers.admin

import java.util.UUID

import controllers.BaseController
import models.result.orderBy.OrderBy
import models.user.Role
import util.FutureUtils.webContext
import services.user.UserSearchService
import util.Application
import util.web.FormUtils

import scala.concurrent.Future

@javax.inject.Singleton
class UserEditController @javax.inject.Inject() (override val app: Application, userSearchService: UserSearchService) extends BaseController {
  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withAdminSession("admin-users") { implicit request =>
      val orderBys = orderBy.map(o => OrderBy(col = o, dir = OrderBy.Direction.fromBoolAsc(orderAsc))).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => app.userService.searchWithCount(query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => app.userService.getAllWithCount(Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      f.map(r => Ok(views.html.admin.user.userList(
        request.identity, q, orderBy, orderAsc, Some(r._1), r._2, limit.getOrElse(100), offset.getOrElse(0)
      )))
    }
  }

  def view(id: UUID) = withAdminSession("admin-user-view") { implicit request =>
    userSearchService.retrieve(id).map { userOpt =>
      val user = userOpt.getOrElse(throw new IllegalStateException(s"Invalid user [$id]."))
      Ok(views.html.admin.user.userView(request.identity, user))
    }
  }

  def edit(id: UUID) = withAdminSession("admin-user-edit") { implicit request =>
    userSearchService.retrieve(id).map { userOpt =>
      val user = userOpt.getOrElse(throw new IllegalStateException(s"Invalid user [$id]."))
      Ok(views.html.admin.user.userEdit(request.identity, user))
    }
  }

  def save(id: UUID) = withAdminSession("admin-user-save") { implicit request =>
    val form = FormUtils.getForm(request)
    userSearchService.retrieve(id).flatMap { userOpt =>
      val user = userOpt.getOrElse(throw new IllegalStateException(s"Invalid user [$id]."))

      val isSelf = request.identity.id == id

      val newUsername = form("username")
      val newEmail = form("email")
      val newPassword = form.get("password") match {
        case Some(x) if x != "original" => Some(x)
        case _ => None
      }
      val role = form.get("role") match {
        case Some("admin") => Role.Admin
        case Some("user") => Role.User
        case x => throw new IllegalStateException(s"Missing role: [$x].")
      }

      if (newUsername.isEmpty) {
        Future.successful(Redirect(controllers.admin.routes.UserEditController.edit(id)).flashing("error" -> "Username is required."))
      } else if (newEmail.isEmpty) {
        Future.successful(Redirect(controllers.admin.routes.UserEditController.edit(id)).flashing("error" -> "Email Address is required."))
      } else if (isSelf && (role != Role.Admin) && user.role == Role.Admin) {
        Future.successful(Redirect(controllers.admin.routes.UserEditController.edit(id)).flashing("error" -> "You cannot remove your own admin role."))
      } else {
        app.userService.update(id, newUsername, newEmail, newPassword, role, user.profile.providerKey).map { _ =>
          Redirect(controllers.admin.routes.UserEditController.view(id))
        }
      }
    }
  }

  def remove(id: UUID) = withAdminSession("admin-user-remove") { implicit request =>
    app.userService.remove(id).map { _ =>
      Redirect(controllers.admin.routes.UserEditController.list())
    }
  }
}
