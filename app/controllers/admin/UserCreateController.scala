package controllers.admin

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.api.{LoginInfo, SignUpEvent}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.BaseController
import models.user.{Role, User, UserPreferences}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.user.UserService
import utils.ApplicationContext
import utils.web.FormUtils

import scala.concurrent.Future

@javax.inject.Singleton
class UserCreateController @javax.inject.Inject() (
    override val ctx: ApplicationContext,
    userService: UserService,
    authInfoRepository: AuthInfoRepository,
    hasher: PasswordHasher
) extends BaseController {
  def newUser() = withAdminSession("admin-user-new") { implicit request =>
    Future.successful(Ok(views.html.admin.user.create(request.identity)))
  }

  def saveNewUser() = withAdminSession("admin-user-save-new") { implicit request =>
    val form = FormUtils.getForm(request)
    val id = UUID.randomUUID
    val loginInfo = LoginInfo(CredentialsProvider.ID, form("email").trim)
    val role = form.get("role") match {
      case Some(r) => Role.withName(r)
      case None => Role.User
    }
    val username = form("username").trim

    if (username.isEmpty) {
      Future.successful(Redirect(controllers.admin.routes.UserCreateController.newUser()).flashing("error" -> messagesApi("error.empty", "Username")))
    } else if (loginInfo.providerKey.isEmpty) {
      Future.successful(Redirect(controllers.admin.routes.UserCreateController.newUser()).flashing("error" -> messagesApi("error.empty", "Email Address")))
    } else {
      val user = User(
        id = id,
        username = username,
        preferences = UserPreferences.empty,
        profile = loginInfo,
        role = role
      )
      val userSavedFuture = userService.save(user)
      val authInfo = hasher.hash(form("password"))
      for {
        authInfo <- authInfoRepository.add(loginInfo, authInfo)
        authenticator <- ctx.silhouette.env.authenticatorService.create(loginInfo)
        value <- ctx.silhouette.env.authenticatorService.init(authenticator)
        userSaved <- userSavedFuture
      } yield {
        ctx.silhouette.env.eventBus.publish(SignUpEvent(userSaved, request))
        Redirect(controllers.admin.routes.UserEditController.view(id)).flashing("success" -> s"User [${form("email")}] added.")
      }
    }
  }
}
