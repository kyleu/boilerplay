package controllers

import java.util.UUID

import models.history.RequestLog
import services.history.RequestHistoryService
import services.user.AuthenticationEnvironment
import play.api.i18n.I18nSupport
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.user.{ UserPreferences, Role, User }
import nl.grons.metrics.scala.FutureMetrics
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ AnyContent, RequestHeader, Result }
import utils.{ DateUtils, Logging }
import utils.metrics.Instrumented

import scala.concurrent.Future

abstract class BaseController() extends Silhouette[User, CookieAuthenticator] with I18nSupport with Instrumented with FutureMetrics with Logging {
  def env: AuthenticationEnvironment

  def withAdminSession(action: String)(block: (SecuredRequest[AnyContent]) => Future[Result]) = SecuredAction.async { implicit request =>
    timing(action) {
      val startTime = System.currentTimeMillis
      if (request.identity.roles.contains(Role.Admin)) {
        block(request).map { r =>
          val duration = (System.currentTimeMillis - startTime).toInt
          logRequest(request, request.identity.id, request.authenticator.loginInfo, duration, r.header.status)
          r
        }
      } else {
        Future.successful(NotFound("404 Not Found"))
      }
    }
  }

  def withSession(action: String)(block: (SecuredRequest[AnyContent]) => Future[Result]) = UserAwareAction.async { implicit request =>
    timing(action) {
      val startTime = System.currentTimeMillis
      val response = request.identity match {
        case Some(user) =>
          val secured = SecuredRequest(user, request.authenticator.getOrElse(throw new IllegalStateException()), request)
          block(secured).map { r =>
            val duration = (System.currentTimeMillis - startTime).toInt
            logRequest(secured, secured.identity.id, secured.authenticator.loginInfo, duration, r.header.status)
            r
          }
        case None =>
          val user = User(
            id = UUID.randomUUID(),
            username = None,
            preferences = UserPreferences(),
            profiles = Nil,
            created = DateUtils.now
          )

          for {
            user <- env.userService.save(user)
            authenticator <- env.authenticatorService.create(LoginInfo("anonymous", user.id.toString))
            value <- env.authenticatorService.init(authenticator)
            result <- block(SecuredRequest(user, authenticator, request))
            authedResponse <- env.authenticatorService.embed(value, result)
          } yield {
            env.eventBus.publish(SignUpEvent(user, request, request2Messages))
            env.eventBus.publish(LoginEvent(user, request, request2Messages))
            val duration = (System.currentTimeMillis - startTime).toInt
            logRequest(request, user.id, authenticator.loginInfo, duration, authedResponse.header.status)
            authedResponse
          }
      }
      response
    }
  }

  private[this] def logRequest(request: RequestHeader, userId: UUID, loginInfo: LoginInfo, duration: Int, status: Int) = {
    val log = RequestLog(request, userId, loginInfo, duration, status)
    RequestHistoryService.insert(log)
  }
}
