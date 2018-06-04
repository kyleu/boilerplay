package util.web

import javax.inject.Inject

import akka.stream.Materializer
import io.prometheus.client.Histogram
import util.FutureUtils.defaultContext
import play.api.mvc._
import util.Logging

import scala.concurrent.Future

class LoggingFilter @Inject() (override implicit val mat: Materializer) extends Filter with Logging {
  val metricsName = util.Config.metricsId + "_http_requests"
  private[this] lazy val requestHistogram = Histogram.build(metricsName, "HTTP request statistics.").register()

  def apply(nextFilter: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    val timer = requestHistogram.startTimer()

    def logCompleted(result: Result): Unit = {
      timer.close()
    }

    nextFilter(request).transform(
      result => {
        logCompleted(result)
        if (request.path.startsWith("/assets")) {
          result
        } else {
          val requestTime = timer.observeDuration()
          log.info(s"${result.header.status} (${requestTime}s): ${request.method} ${request.uri}")
          result.withHeaders("X-Request-Time-Ms" -> (requestTime * 1000).toInt.toString)
        }
      },
      exception => {
        logCompleted(Results.InternalServerError)
        exception
      }
    )
  }
}
