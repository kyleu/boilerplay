package controllers

import brave.Span
import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.auth.AuthEnv
import models.result.data.DataField
import models.user.Role
import play.api.libs.typedmap.TypedKey
import play.api.mvc._
import util.metrics.Instrumented
import util.tracing.TracingHttpHelper
import util.{Application, Logging}

import scala.concurrent.Future

import util.FutureUtils.defaultContext

abstract class BaseController() extends InjectedController with Instrumented with Logging {
  def app: Application

  lazy val name = this.getClass.getSimpleName.stripSuffix("$")

  private[this] val traceKey: TypedKey[Span] = TypedKey.apply[Span]("trace")

  def withoutSession(action: String)(block: UserAwareRequest[AuthEnv, AnyContent] => Future[Result]) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      metrics.timer(action).timeFuture {
        block(request)
      }
    }
  }

  def withSession(action: String, admin: Boolean = false)(block: (SecuredRequest[AuthEnv, AnyContent]) => Future[Result]) = {

    app.silhouette.UserAwareAction.async { implicit request =>
      request.identity match {
        case Some(u) => if (admin && u.role != Role.Admin) {
          failRequest(request)
        } else {
          metrics.timer(action).timeFuture {
            val trace = TracingHttpHelper.traceForRequest(app.tracing.tracer, name, request)
            trace.tag("user.id", u.id.toString)
            trace.tag("user.username", u.username)
            trace.tag("user.email", u.profile.providerKey)
            trace.tag("user.role", u.role.toString)
            val r = SecuredRequest(u, request.authenticator.get, request.addAttr(traceKey, trace))
            val f = block(r)
            f.foreach(result => TracingHttpHelper.completeForResult(trace, result))
            f.failed.foreach(ex => TracingHttpHelper.failed(trace, ex))
            f
          }
        }
        case None => failRequest(request)
      }
    }
  }

  protected def modelForm(rawForm: Option[Map[String, Seq[String]]]) = {
    val form = rawForm.getOrElse(Map.empty).mapValues(_.head)
    val fields = form.toSeq.filter(x => x._1.endsWith(".include") && x._2 == "true").map(_._1.stripSuffix(".include"))
    fields.map(f => DataField(f, Some(form.getOrElse(f, throw new IllegalStateException(s"Cannot find value for included field [$f].")))))
  }

  private[this] def failRequest(request: UserAwareRequest[AuthEnv, AnyContent]) = {
    val msg = request.identity match {
      case Some(_) => "You must be an administrator to access that."
      case None => s"You must sign in or register before accessing ${util.Config.projectName}."
    }
    Future.successful(Redirect(controllers.auth.routes.AuthenticationController.signInForm()).flashing("error" -> msg).withSession(request.session + ("returnUrl" -> request.uri)))
  }
}
