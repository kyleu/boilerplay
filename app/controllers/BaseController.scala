package controllers

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import io.circe.{Json, Printer}
import models.Application
import models.auth.{AuthEnv, UserCredentials}
import com.kyleu.projectile.models.result.data.DataField
import models.user.{Role, SystemUser}
import play.api.http.{ContentTypeOf, Writeable}
import play.api.mvc._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.metrics.Instrumented
import com.kyleu.projectile.util.tracing.TraceData
import com.kyleu.projectile.util.web.{ControllerUtils, TracingFilter}

import scala.language.implicitConversions
import scala.concurrent.{ExecutionContext, Future}

abstract class BaseController(val name: String) extends InjectedController with Logging {
  type Req = SecuredRequest[AuthEnv, AnyContent]

  private[this] def cn(x: Any) = x.getClass.getSimpleName.replaceAllLiterally("$", "")

  protected def app: Application

  protected[this] lazy val metricsName = util.Config.metricsId + "_" + cn(this)

  protected def withoutSession(action: String)(block: UserAwareRequest[AuthEnv, AnyContent] => TraceData => Future[Result])(implicit ec: ExecutionContext) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      Instrumented.timeFuture(metricsName + "_request", "action", name + "_" + action) {
        app.tracing.trace(name + ".controller." + action) { td =>
          enhanceRequest(request, request.identity, td)
          block(request)(td)
        }(getTraceData)
      }
    }
  }

  protected def withSession(action: String, admin: Boolean = false)(block: Req => TraceData => Future[Result])(implicit ec: ExecutionContext) = {
    app.silhouette.UserAwareAction.async { implicit request =>
      request.identity match {
        case Some(u) => if (admin && u.role != Role.Admin) {
          failRequest(request)
        } else {
          Instrumented.timeFuture(metricsName + "_request", "action", name + "_" + action) {
            app.tracing.trace(name + ".controller." + action) { td =>
              enhanceRequest(request, Some(u), td)
              val auth = request.authenticator.getOrElse(throw new IllegalStateException("No auth!"))
              block(SecuredRequest(u, auth, request))(td)
            }(getTraceData)
          }
        }
        case None => failRequest(request)
      }
    }
  }

  protected def getTraceData(implicit requestHeader: RequestHeader) = requestHeader.attrs(TracingFilter.traceKey)

  protected implicit def toCredentials(request: SecuredRequest[AuthEnv, _]): UserCredentials = UserCredentials.fromRequest(request)

  private[this] val defaultPrinter = Printer.spaces2
  protected implicit val contentTypeOfJson: ContentTypeOf[Json] = ContentTypeOf(Some("application/json"))
  protected implicit def writableOfJson(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }

  protected def modelForm(body: AnyContent) = body.asFormUrlEncoded match {
    case Some(x) => ControllerUtils.modelForm(x)
    case None => ControllerUtils.jsonBody(body).as[Seq[DataField]].getOrElse(throw new IllegalStateException("Json must be an array of DataFields."))
  }

  protected def failRequest(request: UserAwareRequest[AuthEnv, AnyContent]) = {
    val msg = request.identity match {
      case Some(_) => "You must be an administrator to access that."
      case None => s"You must sign in or register before accessing ${util.Config.projectName}."
    }
    val res = Redirect(controllers.auth.routes.AuthenticationController.signInForm())
    Future.successful(res.flashing("error" -> msg).withSession(request.session + ("returnUrl" -> request.uri)))
  }

  def enhanceRequest(request: Request[AnyContent], user: Option[SystemUser], trace: TraceData) = {
    trace.tag("http.request.size", request.body.asText.map(_.length).orElse(request.body.asRaw.map(_.size.toInt)).getOrElse(0).toString)
    user.foreach { u =>
      trace.tag("user.id", u.id.toString)
      trace.tag("user.username", u.username)
      trace.tag("user.email", u.profile.providerKey)
      trace.tag("user.role", u.role.toString)
    }
  }
}
