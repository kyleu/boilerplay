package controllers

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import models.auth.AuthEnv
import models.result.data.DataField
import models.user.Role
import util.FutureUtils.webContext
import play.api.mvc._
import util.metrics.Instrumented
import util.{Application, Logging}

import scala.concurrent.Future

abstract class BaseController() extends InjectedController with Instrumented with Logging {
  def app: Application

  lazy val name = this.getClass.getSimpleName.stripSuffix("$")

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
            val trace = app.tracing.traceForRequest(name, request)
            trace.foreach { t =>
              t.tag("user.id", u.id.toString)
              t.tag("user.username", u.username)
              t.tag("user.email", u.profile.providerKey)
              t.tag("user.role", u.role.toString)
            }

            val f = block(SecuredRequest(u, request.authenticator.get, request))
            f.foreach(result => app.tracing.complete(trace, result))
            f.failed.foreach(ex => app.tracing.failed(trace, ex))
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
