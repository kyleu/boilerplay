package util.tracing

import brave.{Span, Tracer}
import play.api.mvc.{AnyContent, Request, Result}
import zipkin.{Endpoint, TraceKeys}

object TracingHttpHelper {
  def traceForRequest(tracer: Tracer, controller: String, r: Request[AnyContent]) = {
    val trace = tracer.newTrace()
    trace.tag(TraceKeys.HTTP_PATH, r.path)
    trace.tag(TraceKeys.HTTP_METHOD, r.method)
    trace.tag(TraceKeys.HTTP_HOST, r.host)
    trace.tag(TraceKeys.HTTP_REQUEST_SIZE, r.body.asRaw.size.toString)
    trace.remoteEndpoint(Endpoint.builder().serviceName(controller).ipv4(127 << 24 | 1).port(1234).build())
    trace.start()
  }

  def completeForResult(trace: Span, result: Result) = {
    trace.tag(TraceKeys.HTTP_STATUS_CODE, result.header.status.toString)
    result.body.contentLength.foreach { size =>
      trace.tag(TraceKeys.HTTP_RESPONSE_SIZE, size.toString)
    }
    trace.finish()
  }

  def failed(trace: Span, ex: Throwable) = {
    trace.tag("exception", ex.toString)
    trace.finish()
  }
}
