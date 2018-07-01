package util.web

import java.util.concurrent.TimeUnit

import javax.inject.Inject
import akka.stream.Materializer
import util.FutureUtils.defaultContext
import play.api.mvc._
import util.Logging
import util.metrics.Instrumented

import scala.concurrent.Future

class LoggingFilter @Inject() (override implicit val mat: Materializer) extends Filter with Logging {
  val metricsName = util.Config.metricsId + "_http_requests"

  def apply(nextFilter: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    val startNanos = System.nanoTime

    def logCompleted(result: Result): Unit = {
      Instrumented.regOpt.foreach(_.counter(metricsName + "_count", "result", result.getClass.getSimpleName).increment())
    }

    nextFilter(request).transform(
      result => {
        if (request.path.startsWith("/assets")) {
          result
        } else {
          logCompleted(result)
          val requestTime = System.nanoTime - startNanos
          Instrumented.regOpt.foreach(_.timer(metricsName, "result", result.getClass.getSimpleName).record(requestTime, TimeUnit.NANOSECONDS))
          log.info(s"${result.header.status} (${requestTime / 1000000000.0}s): ${request.method} ${request.uri}")
          result.withHeaders("X-Request-Time-Ms" -> (requestTime * 1000000).toInt.toString)
        }
      },
      exception => {
        logCompleted(Results.InternalServerError)
        exception
      }
    )
  }
}
