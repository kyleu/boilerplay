package util.web

import play.api.libs.ws._
import util.tracing.{TraceData, TracingService}
import zipkin.TraceKeys

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

private class TracingWSRequest(
    val spanName: String, val request: WSRequest, val tracer: TracingService, val traceData: TraceData
)(implicit val ctx: ExecutionContext) extends TracingWSRequestHelper {
  override def sign(calc: WSSignatureCalculator) = new TracingWSRequest(spanName, request.sign(calc), tracer, traceData)
  override def withAuth(username: String, password: String, scheme: WSAuthScheme) = {
    new TracingWSRequest(spanName, request.withAuth(username, password, scheme), tracer, traceData)
  }
  @deprecated("Use withHttpHeaders or addHttpHeaders", "1.0.0")
  override def withHeaders(hdrs: (String, String)*) = new TracingWSRequest(spanName, request.withHttpHeaders(hdrs: _*), tracer, traceData)
  @deprecated("Use withQueryStringParameters or addQueryStringParameter", "1.0.0")
  override def withQueryString(parameters: (String, String)*) = {
    new TracingWSRequest(spanName, request.withQueryStringParameters(parameters: _*), tracer, traceData)
  }
  override def withFollowRedirects(follow: Boolean) = new TracingWSRequest(spanName, request.withFollowRedirects(follow), tracer, traceData)
  override def withRequestTimeout(timeout: Duration) = new TracingWSRequest(spanName, request.withRequestTimeout(timeout), tracer, traceData)
  override def withRequestFilter(filter: WSRequestFilter) = new TracingWSRequest(spanName, request.withRequestFilter(filter), tracer, traceData)
  override def withVirtualHost(vh: String) = new TracingWSRequest(spanName, request.withVirtualHost(vh), tracer, traceData)
  override def withProxyServer(proxyServer: WSProxyServer) = new TracingWSRequest(spanName, request.withProxyServer(proxyServer), tracer, traceData)
  override def withMethod(method: String) = new TracingWSRequest(spanName, request.withMethod(method), tracer, traceData)

  override def execute() = tracer.trace(spanName) { data =>
    annotate(data, "execute")
    request.addHttpHeaders(tracer.toMap(data.span).toSeq: _*).execute().map { rsp =>
      data.span.tag(TraceKeys.HTTP_STATUS_CODE, rsp.status.toString)
      data.span.tag(TraceKeys.HTTP_RESPONSE_SIZE, rsp.bodyAsBytes.size.toString)
      rsp
    }
  }(traceData)
  override def stream() = tracer.trace(spanName) { data =>
    annotate(data, "stream")
    request.addHttpHeaders(tracer.toMap(data.span).toSeq: _*).stream()
  }(traceData)

  override def execute(method: String) = withMethod(method).execute()

  override def withHttpHeaders(headers: (String, String)*) = new TracingWSRequest(spanName, request.withHttpHeaders(headers: _*), tracer, traceData)
  override def withQueryStringParameters(parameters: (String, String)*) = {
    new TracingWSRequest(spanName, request.withQueryStringParameters(parameters: _*), tracer, traceData)
  }
  override def withCookies(cookies: WSCookie*) = new TracingWSRequest(spanName, request.withCookies(cookies: _*), tracer, traceData)

  private[this] def annotate(data: TraceData, callType: String) = {
    data.span.tag("call", callType)
    data.span.tag(TraceKeys.HTTP_URL, request.url)
    data.span.tag(TraceKeys.HTTP_METHOD, request.method)
    request.headers.get(play.api.http.HeaderNames.CONTENT_LENGTH).map(x => data.span.tag(TraceKeys.HTTP_REQUEST_SIZE, x.headOption.getOrElse("0").toString))
    request.header("Content-Type").foreach(ct => data.span.tag("contenttype", ct))
    request.requestTimeout.foreach(t => data.span.tag("timeout", t.toString))
    data
  }
}
