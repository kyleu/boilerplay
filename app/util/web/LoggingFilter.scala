package util.web

import javax.inject.Inject

import akka.stream.Materializer
import com.codahale.metrics.Meter
import play.api.http.Status
import util.FutureUtils.defaultContext
import play.api.mvc._
import util.Logging
import util.metrics.Instrumented

import scala.concurrent.Future

class LoggingFilter @Inject() (override implicit val mat: Materializer) extends Filter with Logging with Instrumented {
  val prefix = "boilerplay.requests."

  val knownStatuses = Seq(
    Status.OK, Status.BAD_REQUEST, Status.FORBIDDEN, Status.NOT_FOUND,
    Status.CREATED, Status.TEMPORARY_REDIRECT, Status.INTERNAL_SERVER_ERROR, Status.CONFLICT,
    Status.UNAUTHORIZED, Status.NOT_MODIFIED
  )

  lazy val statusCodes: Map[Int, Meter] = knownStatuses.map(s => s -> metricRegistry.meter(prefix + s.toString)).toMap

  lazy val requestsTimer = metricRegistry.timer(s"${prefix}requestTimer")
  lazy val activeRequests = metricRegistry.counter(s"${prefix}activeRequests")
  lazy val otherStatuses = metricRegistry.meter(s"${prefix}other")

  def apply(nextFilter: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    val context = requestsTimer.time()
    activeRequests.inc()

    def logCompleted(result: Result): Unit = {
      activeRequests.dec()
      context.stop()
      statusCodes.getOrElse(result.header.status, otherStatuses).mark()
    }

    nextFilter(request).transform(
      result => {
        logCompleted(result)
        if (request.path.startsWith("/assets")) {
          result
        } else {
          val endTime = System.currentTimeMillis
          val requestTime = endTime - startTime
          log.info(s"${result.header.status} (${requestTime}ms): ${request.method} ${request.uri}")
          result.withHeaders("X-Request-Time-Ms" -> requestTime.toString)
        }
      },
      exception => {
        logCompleted(Results.InternalServerError)
        exception
      }
    )
  }
}
