package controllers.admin.user

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.BaseController
import models.Application
import models.user.{Role, User, UserPreferences}
import util.web.ControllerUtils

import scala.concurrent.Future

@javax.inject.Singleton
class UserController @javax.inject.Inject() (
    override val app: Application,
    authInfoRepository: AuthInfoRepository,
    hasher: PasswordHasher
) extends BaseController("user.create") with UserEditHelper {
  import app.contexts.webContext

  def createForm = withSession("user.createForm", admin = true) { implicit request => implicit td =>
    val call = controllers.admin.user.routes.UserController.create()
    Future.successful(Ok(views.html.admin.user.userForm(request.identity, models.user.User.empty, "New User", call, isNew = true)))
  }

  def create() = withSession("user.create", admin = true) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request)
    val id = UUID.randomUUID
    val loginInfo = LoginInfo(CredentialsProvider.ID, form("email").trim)
    val role = form.get("role") match {
      case Some(r) => Role.withName(r)
      case None => Role.User
    }
    val username = form("username").trim

    if (username.isEmpty) {
      Future.successful(Redirect(controllers.admin.user.routes.UserController.createForm()).flashing("error" -> "Username is required."))
    } else if (loginInfo.providerKey.isEmpty) {
      Future.successful(Redirect(controllers.admin.user.routes.UserController.createForm()).flashing("error" -> "Email Address is required."))
    } else {
      val user = User(
        id = id,
        username = username,
        preferences = UserPreferences.empty,
        profile = loginInfo,
        role = role
      )
      val userSavedFuture = app.userService.insert(user)
      val authInfo = hasher.hash(form("password"))
      for {
        _ <- authInfoRepository.add(loginInfo, authInfo)
        authenticator <- app.silhouette.env.authenticatorService.create(loginInfo)
        _ <- app.silhouette.env.authenticatorService.init(authenticator)
        userSaved <- userSavedFuture
      } yield {
        app.silhouette.env.eventBus.publish(SignUpEvent(userSaved, request))
        Redirect(controllers.admin.user.routes.UserController.view(id)).flashing("success" -> s"User [${form("email")}] added.")
      }
    }
  }
}
