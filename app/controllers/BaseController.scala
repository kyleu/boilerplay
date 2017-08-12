package controllers

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.auth.AuthEnv
import util.FutureUtils.webContext
import play.api.mvc._
import util.metrics.Instrumented
import util.{Application, Logging}

import scala.concurrent.Future

abstract class BaseController() extends InjectedController with Instrumented with Logging {
  def app: Application

  def withAdminSession(action: String)(block: (SecuredRequest[AuthEnv, AnyContent]) => Future[Result]) = {
    app.silhouette.SecuredAction.async { implicit request =>
      metrics.timer(action).timeFuture {
        if (request.identity.isAdmin) {
          block(request)
        } else {
          Future.successful(Redirect(controllers.routes.HomeController.home()).flashing("error" -> "You must have admin rights to access that page."))
        }
      }
    }
  }

  def withoutSession(action: String)(block: UserAwareRequest[AuthEnv, AnyContent] => Future[Result]) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      metrics.timer(action).timeFuture {
        block(request)
      }
    }
  }

  def withSession(action: String)(block: (SecuredRequest[AuthEnv, AnyContent]) => Future[Result]) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      metrics.timer(action).timeFuture {
        request.identity match {
          case Some(u) =>
            val auth = request.authenticator.getOrElse(throw new IllegalStateException("Somehow, you're not logged in."))
            block(SecuredRequest(u, auth, request))
          case None =>
            val result = app.userService.countAll().map { count =>
              if (count == 0) {
                Redirect(controllers.auth.routes.RegistrationController.registrationForm())
              } else {
                Redirect(controllers.auth.routes.AuthenticationController.signInForm())
              }
            }

            val flashed = result.map(_.flashing(
              "error" -> s"You must sign in or register before accessing ${util.Config.projectName}."
            ))

            flashed.map { r =>
              if (!request.uri.contains("signin")) {
                r.withSession(r.session + ("returnUrl" -> request.uri))
              } else {
                log.info(s"Skipping returnUrl for external url [${request.uri}].")
                r
              }
            }
        }
      }
    }
  }
}
