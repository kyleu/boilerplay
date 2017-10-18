package controllers.admin.user

import java.util.UUID

import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._

import models.result.orderBy.OrderBy
import models.user.{Role, UserResult}
import util.web.ControllerUtils

import scala.concurrent.Future

trait UserEditHelper { this: UserController =>
  import app.contexts.webContext

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("user.list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(col = orderBy, asc = orderAsc).toSeq
      val r = q match {
        case Some(query) if query.nonEmpty => app.userService.searchWithCount(query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => app.userService.getAllWithCount(Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      Future.successful(render {
        case Accepts.Html() => Ok(views.html.admin.user.userList(
          request.identity, q, orderBy, orderAsc, Some(r._1), r._2, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(UserResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson.spaces2).as(JSON)
      })
    }
  }

  def view(id: UUID) = withSession("user.view", admin = true) { implicit request => implicit td =>
    app.userService.getByPrimaryKey(id) match {
      case Some(model) => Future.successful(render {
        case Accepts.Html() => Ok(views.html.admin.user.userView(request.identity, model))
        case Accepts.Json() => Ok(model.asJson.spaces2).as(JSON)
      })
      case None => Future.successful(NotFound(s"No user found with id [$id]."))
    }
  }

  def editForm(id: UUID) = withSession("user.edit.form", admin = true) { implicit request => implicit td =>
    val call = controllers.admin.user.routes.UserController.edit(id)
    app.userService.getByPrimaryKey(id) match {
      case Some(model) => Future.successful(Ok(views.html.admin.user.userForm(request.identity, model, s"User [$id]", call)))
      case None => Future.successful(NotFound(s"No user found with id [$id]."))
    }
  }

  def edit(id: UUID) = withSession("admin.user.save", admin = true) { implicit request => implicit td =>
    val user = app.userService.getByPrimaryKey(id).getOrElse(util.ise(s"Invalid user [$id]."))
    val isSelf = request.identity.id == id

    val form = ControllerUtils.getForm(request)
    val newUsername = form("username")
    val newEmail = form("email")
    val newPassword = form.get("password") match {
      case Some(x) if x != "original" => Some(x)
      case _ => None
    }
    val role = form.get("role") match {
      case Some("admin") => Role.Admin
      case Some("user") => Role.User
      case x => util.ise(s"Missing role: [$x].")
    }

    if (newUsername.isEmpty) {
      Future.successful(Redirect(controllers.admin.user.routes.UserController.edit(id)).flashing("error" -> "Username is required."))
    } else if (newEmail.isEmpty) {
      Future.successful(Redirect(controllers.admin.user.routes.UserController.edit(id)).flashing("error" -> "Email Address is required."))
    } else if (isSelf && (role != Role.Admin) && user.role == Role.Admin) {
      Future.successful(Redirect(controllers.admin.user.routes.UserController.edit(id)).flashing("error" -> "You cannot remove your own admin role."))
    } else {
      app.userService.updateFields(id, newUsername, newEmail, newPassword, role, user.profile.providerKey)
      Future.successful(Redirect(controllers.admin.user.routes.UserController.view(id)))
    }
  }

  def remove(id: UUID) = withSession("admin.user.remove", admin = true) { implicit request => implicit td =>
    app.userService.remove(id)
    Future.successful(Redirect(controllers.admin.user.routes.UserController.list()))
  }
}
