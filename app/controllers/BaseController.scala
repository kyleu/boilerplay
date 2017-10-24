package controllers

import java.net.InetAddress

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.Application
import models.audit.{AuditModelPk, AuditStart}
import models.auth.{AuthEnv, Credentials}
import models.user.Role
import play.api.mvc._
import util.Logging
import util.metrics.Instrumented
import util.tracing.TraceData
import util.web.TracingFilter
import scala.language.implicitConversions

import scala.concurrent.{ExecutionContext, Future}

abstract class BaseController(val name: String) extends InjectedController with Instrumented with Logging {
  protected def app: Application

  protected def withoutSession(action: String)(
    block: UserAwareRequest[AuthEnv, AnyContent] => TraceData => Future[Result]
  )(implicit ec: ExecutionContext) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      metrics.timer(name + "." + action).timeFuture {
        app.tracing.trace(name + ".controller." + action) { td =>
          ControllerUtilities.enhanceRequest(request, request.identity, td.span)
          block(request)(td)
        }(getTraceData)
      }
    }
  }

  protected def withSession(action: String, admin: Boolean = false)(
    block: SecuredRequest[AuthEnv, AnyContent] => TraceData => Future[Result]
  )(implicit ec: ExecutionContext) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      request.identity match {
        case Some(u) => if (admin && u.role != Role.Admin) {
          failRequest(request)
        } else {
          metrics.timer(name + "." + action).timeFuture {
            app.tracing.trace(name + ".controller." + action) { td =>
              ControllerUtilities.enhanceRequest(request, Some(u), td.span)
              val r = SecuredRequest(u, request.authenticator.get, request)
              block(r)(td)
            }(getTraceData)
          }
        }
        case None => failRequest(request)
      }
    }
  }

  protected def getTraceData(implicit requestHeader: RequestHeader) = requestHeader.attrs(TracingFilter.traceKey)

  protected implicit def toCredentials(request: SecuredRequest[AuthEnv, _]) = Credentials.fromRequest(request)

  def pk(t: String, v: Any*) = AuditModelPk(t, v.map(_.toString))
  def audit(models: AuditModelPk*)(
    creds: Credentials, act: String = "development", tags: Map[String, String] = Map.empty
  )(implicit request: Request[AnyContent]) = {
    AuditStart(action = act, app = Some(util.Config.projectId), client = request.remoteAddress, tags = tags, models = models)
  }

  protected def modelForm(rawForm: Option[Map[String, Seq[String]]]) = ControllerUtilities.modelForm(rawForm)

  private[this] def failRequest(request: UserAwareRequest[AuthEnv, AnyContent]) = {
    val msg = request.identity match {
      case Some(_) => "You must be an administrator to access that."
      case None => s"You must sign in or register before accessing ${util.Config.projectName}."
    }
    val res = Redirect(controllers.auth.routes.AuthenticationController.signInForm())
    Future.successful(res.flashing("error" -> msg).withSession(request.session + ("returnUrl" -> request.uri)))
  }
}
