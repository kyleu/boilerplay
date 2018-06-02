package controllers

import io.circe.{Json, Printer}
import io.prometheus.client.Histogram
import models.Application
import play.api.http.{ContentTypeOf, Writeable}
import play.api.mvc._
import util.Logging
import util.metrics.Instrumented
import util.tracing.TraceData
import util.web.{ControllerUtils, TracingFilter}

import scala.language.implicitConversions
import scala.concurrent.{ExecutionContext, Future}

abstract class BaseController(val name: String) extends InjectedController with Logging {
  type Req = Request[AnyContent]

  private[this] def cn(x: Any) = x.getClass.getSimpleName.replaceAllLiterally("$", "")

  protected def app: Application

  protected[this] lazy val metricsName = util.Config.projectId + "_" + cn(this)
  protected[this] lazy val requestHistogram = Histogram.build(
    metricsName + "_request",
    s"Controller request metrics for [$metricsName]"
  ).labelNames("method").register()

  protected def act(action: String)(block: Req => TraceData => Future[Result])(implicit ec: ExecutionContext) = {
    Action.async { implicit request =>
      Instrumented.timeFuture(requestHistogram, name + "_" + action) {
        app.tracing.trace(name + ".controller." + action) { td =>
          ControllerUtils.enhanceRequest(request, td)
          block(request)(td)
        }(getTraceData)
      }
    }
  }

  protected def getTraceData(implicit requestHeader: RequestHeader) = requestHeader.attrs(TracingFilter.traceKey)

  private[this] val defaultPrinter = Printer.spaces2
  protected implicit val contentTypeOfJson: ContentTypeOf[Json] = ContentTypeOf(Some("application/json"))
  protected implicit def writableOfJson(implicit codec: Codec, printer: Printer = defaultPrinter): Writeable[Json] = {
    Writeable(a => codec.encode(a.pretty(printer)))
  }
}
