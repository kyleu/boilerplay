package controllers

import java.net.InetAddress

import brave.Span
import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import io.circe.Json
import models.Application
import models.audit.{AuditModelPk, AuditStart}
import models.auth.AuthEnv
import models.result.data.DataField
import models.user.{Role, User}
import play.api.mvc._
import util.metrics.Instrumented
import util.web.TracingFilter
import util.Logging
import util.tracing.TraceData
import zipkin.TraceKeys
import sangria.marshalling.MarshallingUtil._
import sangria.marshalling.circe._

import scala.concurrent.{ExecutionContext, Future}

abstract class BaseController(val name: String) extends InjectedController with Instrumented with Logging {
  private[this] lazy val serverName = Some(InetAddress.getLocalHost.getHostName)

  protected def app: Application

  protected def withoutSession(action: String)(
    block: UserAwareRequest[AuthEnv, AnyContent] => TraceData => Future[Result]
  )(implicit ec: ExecutionContext) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      metrics.timer(name + "." + action).timeFuture {
        app.tracing.trace(name + ".controller." + action) { td =>
          enhanceRequest(request, request.identity, td.span)
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
              enhanceRequest(request, Some(u), td.span)
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

  def pk(t: String, v: Any*) = AuditModelPk(t, v.map(_.toString))
  def audit(models: AuditModelPk*)(user: User, act: String = "development", tags: Map[String, String] = Map.empty)(implicit request: Request[AnyContent]) = {
    val c = Some(request.remoteAddress)
    AuditStart(action = act, app = Some(util.Config.projectId), client = c, server = serverName, user = Some(user.id), tags = tags, models = models)
  }

  def auditNoUser(models: AuditModelPk*)(act: String = "development", tags: Map[String, String] = Map.empty)(implicit request: Request[AnyContent]) = {
    val c = Some(request.remoteAddress)
    AuditStart(action = act, app = Some(util.Config.projectId), client = c, server = serverName, user = None, tags = tags, models = models)
  }

  protected def modelForm(rawForm: Option[Map[String, Seq[String]]]) = {
    val form = rawForm.getOrElse(Map.empty).mapValues(_.head)
    val fields = form.toSeq.filter(x => x._1.endsWith(".include") && x._2 == "true").map(_._1.stripSuffix(".include"))
    fields.map(f => DataField(f, Some(form.getOrElse(f, util.ise(s"Cannot find value for included field [$f].")))))
  }

  protected def jsonFor(request: Request[AnyContent]) = {
    import sangria.marshalling.playJson._
    val playJson = request.body.asJson.getOrElse(util.ise("Invalid JSON."))
    playJson.convertMarshaled[Json]
  }

  private[this] def enhanceRequest(request: Request[AnyContent], user: Option[User], trace: Span) = {
    trace.tag(TraceKeys.HTTP_REQUEST_SIZE, request.body.asText.map(_.length).orElse(request.body.asRaw.map(_.size)).getOrElse(0).toString)
    user.foreach { u =>
      trace.tag("user.id", u.id.toString)
      trace.tag("user.username", u.username)
      trace.tag("user.email", u.profile.providerKey)
      trace.tag("user.role", u.role.toString)
    }
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
