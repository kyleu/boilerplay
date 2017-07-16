package util.web

import javax.inject.Inject
import play.api.http._
import play.api.mvc.RequestHeader
import play.api.routing.Router
import util.Logging

class RequestHandler @Inject() (
    errorHandler: HttpErrorHandler,
    configuration: HttpConfiguration,
    filters: HttpFilters,
    router: Router
) extends DefaultHttpRequestHandler(router, errorHandler, configuration, filters) with Logging {

  override def routeRequest(request: RequestHeader) = {
    if (!Option(request.path).exists(_.startsWith("/assets"))) {
      log.info(s"Request from [${request.remoteAddress}]: ${request.toString()}")
    }
    super.routeRequest(request)
  }
}
