package utils.web

import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.routing.Router
import utils.Logging
import scala.concurrent._

class ErrorHandler @Inject() (
    env: Environment, config: Configuration, sourceMapper: OptionalSourceMapper, router: Provider[Router]
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) with Logging {

  override def onProdServerError(request: RequestHeader, ex: UsefulException) = Future.successful(
    Results.InternalServerError(views.html.error.serverError(request.path, Some(ex))(request.session, request.flash))
  )

  override def onClientError(request: RequestHeader, statusCode: Int, message: String) = Future.successful(
    Results.NotFound(views.html.error.notFound(request.path)(request.session, request.flash))
  )

  override protected def onBadRequest(request: RequestHeader, error: String) = Future.successful(
    Results.BadRequest(views.html.error.badRequest(request.path, error)(request.session, request.flash))
  )
}
