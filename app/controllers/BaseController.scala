package controllers

import brave.Span
import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.auth.AuthEnv
import models.result.data.DataField
import models.user.{Role, User}
import play.api.mvc._
import util.metrics.Instrumented
import util.web.TracingFilter
import util.{Application, Logging}
import zipkin.{Endpoint, TraceKeys}
import util.FutureUtils.defaultContext

import scala.concurrent.Future

abstract class BaseController() extends InjectedController with Instrumented with Logging {
  def app: Application

  lazy val name = this.getClass.getSimpleName.stripSuffix("$")

  def withoutSession(action: String)(block: UserAwareRequest[AuthEnv, AnyContent] => Future[Result]) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      metrics.timer(action).timeFuture {
        enhanceRequest(request, None, getTraceData.span)
        block(request)
      }
    }
  }

  def withSession(action: String, admin: Boolean = false)(block: SecuredRequest[AuthEnv, AnyContent] => Future[Result]) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      request.identity match {
        case Some(u) => if (admin && u.role != Role.Admin) {
          failRequest(request)
        } else {
          metrics.timer(action).timeFuture {
            enhanceRequest(request, Some(u), getTraceData.span)
            val r = SecuredRequest(u, request.authenticator.get, request)
            block(r)
          }
        }
        case None => failRequest(request)
      }
    }
  }

  protected implicit def getTraceData(implicit requestHeader: RequestHeader) = requestHeader.attrs(TracingFilter.traceKey)

  protected def modelForm(rawForm: Option[Map[String, Seq[String]]]) = {
    val form = rawForm.getOrElse(Map.empty).mapValues(_.head)
    val fields = form.toSeq.filter(x => x._1.endsWith(".include") && x._2 == "true").map(_._1.stripSuffix(".include"))
    fields.map(f => DataField(f, Some(form.getOrElse(f, throw new IllegalStateException(s"Cannot find value for included field [$f].")))))
  }

  private[this] def enhanceRequest(request: Request[AnyContent], u: Option[User], trace: Span) = {
    trace.tag(TraceKeys.HTTP_REQUEST_SIZE, request.body.asRaw.size.toString)
    trace.remoteEndpoint(Endpoint.builder().serviceName(name).ipv4(127 << 24 | 1).port(1234).build())
  }

  private[this] def failRequest(request: UserAwareRequest[AuthEnv, AnyContent]) = {
    val msg = request.identity match {
      case Some(_) => "You must be an administrator to access that."
      case None => s"You must sign in or register before accessing ${util.Config.projectName}."
    }
    val res = Redirect(controllers.auth.routes.AuthenticationController.signInForm())
    Future.successful(res.flashing("error" -> msg).withSession(request.session + ("returnUrl" -> request.uri)))
  }
}
