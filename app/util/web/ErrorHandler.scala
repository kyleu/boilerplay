package util.web

import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.routing.Router
import util.Logging
import util.tracing.TracingService

import scala.concurrent._

class ErrorHandler @Inject() (
    env: Environment, config: Configuration, sourceMapper: OptionalSourceMapper, router: Provider[Router], tracing: TracingService
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) with Logging {

  override def onProdServerError(request: RequestHeader, ex: UsefulException) = tracing.topLevelTrace("error.prod") { td =>
    td.span.tag("error.type", ex.getClass.getSimpleName)
    td.span.tag("error.message", ex.getMessage)
    td.span.tag("error.stack", ex.getStackTrace.mkString("\n"))
    Future.successful(Results.InternalServerError(views.html.error.serverError(request.path, Some(ex))(request.session, request.flash, td)))
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String) = tracing.topLevelTrace("not.found") { td =>
    td.span.tag("error.type", "client.error")
    td.span.tag("error.message", message)
    Future.successful(Results.NotFound(views.html.error.notFound(request.path)(request.session, request.flash, td)))
  }

  override protected def onBadRequest(request: RequestHeader, error: String) = tracing.topLevelTrace("not.found") { td =>
    td.span.tag("error.type", "bad.request")
    td.span.tag("error.message", error)
    Future.successful(Results.BadRequest(views.html.error.badRequest(request.path, error)(request.session, request.flash, td)))
  }
}
