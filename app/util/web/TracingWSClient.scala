package util.web

import play.api.libs.ws._
import util.tracing.{TraceData, TracingService}

import scala.concurrent.ExecutionContext

class TracingWSClient @javax.inject.Inject() (ws: WSClient, tracer: TracingService) {
  def underlying[T]: T = ws.underlying
  def url(spanName: String, url: String)(implicit traceData: TraceData, ctx: ExecutionContext): WSRequest = {
    new TracingWSRequest(spanName, ws.url(url), tracer, traceData)
  }
  def close(): Unit = ws.close()
}
