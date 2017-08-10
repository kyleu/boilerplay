package controllers.admin

import java.util.UUID

import controllers.BaseController
import models.user.Role
import util.FutureUtils.defaultContext
import services.user.UserSearchService
import util.Application
import util.web.FormUtils

import scala.concurrent.Future

@javax.inject.Singleton
class UserEditController @javax.inject.Inject() (override val app: Application, userSearchService: UserSearchService) extends BaseController {
  def list(q: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = withAdminSession("admin-users") { implicit request =>
    val f = q match {
      case Some(query) if query.nonEmpty => app.userService.search(query, limit.orElse(Some(100)), offset)
      case _ => app.userService.getAll(limit.orElse(Some(100)), offset)
    }
    val c = q match {
      case Some(query) if query.nonEmpty => app.userService.searchCount(query)
      case _ => app.userService.totalCount()
    }
    for (models <- f; total <- c) yield {
      Ok(views.html.admin.user.userList(request.identity, q, Some(total), models, limit.getOrElse(100), offset.getOrElse(0)))
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
