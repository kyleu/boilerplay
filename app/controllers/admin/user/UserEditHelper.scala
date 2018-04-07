package controllers.admin.user

import java.util.UUID

import com.mohiva.play.silhouette.api.{LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.user.{Role, SystemUser, UserPreferences}
import util.web.ControllerUtils

import scala.concurrent.Future

trait UserEditHelper { this: SystemUserController =>
  import app.contexts.webContext
  def createForm = withSession("user.createForm", admin = true) { implicit request => implicit td =>
    val call = controllers.admin.user.routes.SystemUserController.create()
    Future.successful(Ok(views.html.admin.user.systemUserForm(request.identity, models.user.SystemUser.empty(), "New User", call, isNew = true)))
  }

  def create() = withSession("user.create", admin = true) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val id = UUID.randomUUID
    val loginInfo = LoginInfo(CredentialsProvider.ID, form("email").trim)
    val role = form.get("role") match {
      case Some(r) => Role.withValue(r)
      case None => Role.User
    }
    val username = form("username").trim

    if (username.isEmpty) {
      Future.successful(Redirect(controllers.admin.user.routes.SystemUserController.createForm()).flashing("error" -> "Username is required."))
    } else if (loginInfo.providerKey.isEmpty) {
      Future.successful(Redirect(controllers.admin.user.routes.SystemUserController.createForm()).flashing("error" -> "Email Address is required."))
    } else {
      val user = SystemUser(
        id = id,
        username = username,
        preferences = UserPreferences.empty,
        profile = loginInfo,
        role = role
      )
      app.coreServices.users.insert(request, user).flatMap { userSaved =>
        val authInfo = hasher.hash(form("password"))
        for {
          _ <- authInfoRepository.add(loginInfo, authInfo)
          authenticator <- app.silhouette.env.authenticatorService.create(loginInfo)
          _ <- app.silhouette.env.authenticatorService.init(authenticator)
        } yield {
          app.silhouette.env.eventBus.publish(SignUpEvent(userSaved, request))
          Redirect(controllers.admin.user.routes.SystemUserController.view(id)).flashing("success" -> s"User [${form("email")}] added.")
        }
      }
    }
  }

  def editForm(id: UUID) = withSession("user.edit.form", admin = true) { implicit request => implicit td =>
    val call = controllers.admin.user.routes.SystemUserController.edit(id)
    app.coreServices.users.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(views.html.admin.user.systemUserForm(request.identity, model, s"User [$id]", call))
      case None => NotFound(s"No user found with id [$id].")
    }
  }

  def edit(id: UUID) = withSession("admin.user.save", admin = true) { implicit request => implicit td =>
    app.coreServices.users.getByPrimaryKey(request, id).map { u =>
      val user = u.getOrElse(throw new IllegalStateException(s"Invalid user [$id]."))
      val isSelf = request.identity.id == id

      val form = ControllerUtils.getForm(request.body)
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
        Redirect(controllers.admin.user.routes.SystemUserController.edit(id)).flashing("error" -> "Username is required.")
      } else if (newEmail.isEmpty) {
        Redirect(controllers.admin.user.routes.SystemUserController.edit(id)).flashing("error" -> "Email Address is required.")
      } else if (isSelf && (role != Role.Admin) && user.role == Role.Admin) {
        Redirect(controllers.admin.user.routes.SystemUserController.edit(id)).flashing("error" -> "You cannot remove your own admin role.")
      } else {
        app.coreServices.users.updateFields(request, id, newUsername, CredentialsProvider.ID, newEmail, newPassword, role, user.profile.providerKey)
        Redirect(controllers.admin.user.routes.SystemUserController.view(id))
      }
    }
  }

  def remove(id: UUID) = withSession("admin.user.remove", admin = true) { implicit request => implicit td =>
    app.coreServices.users.remove(request, id)
    Future.successful(Redirect(controllers.admin.user.routes.SystemUserController.list()))
  }
}
