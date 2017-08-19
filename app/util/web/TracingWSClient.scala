package util.web

import play.api.libs.ws._
import util.tracing.{TraceData, TracingService}

class TracingWSClient @javax.inject.Inject() (ws: WSClient, tracer: TracingService) {
  def underlying[T]: T = ws.underlying
  def url(url: String): WSRequest = ws.url(url)
  def url(spanName: String, url: String)(implicit traceData: TraceData): WSRequest = new TracingWSRequest(spanName, ws.url(url), tracer, traceData)
  def close(): Unit = ws.close()
}
