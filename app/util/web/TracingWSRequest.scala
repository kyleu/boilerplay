package util.web

import java.io.File

import akka.stream.scaladsl.Source
import akka.util.ByteString
import play.api.libs.ws._
import play.api.mvc.MultipartFormData.Part
import util.tracing.{TraceData, TracingService}

import scala.concurrent.duration.Duration

private class TracingWSRequest(spanName: String, request: WSRequest, tracer: TracingService, traceData: TraceData) extends WSRequest {
  override val url: String = request.url
  override val method: String = request.method
  override val body: WSBody = request.body
  override val headers: Map[String, Seq[String]] = request.headers
  override val queryString: Map[String, Seq[String]] = request.queryString
  override val calc: Option[WSSignatureCalculator] = request.calc
  override val auth: Option[(String, String, WSAuthScheme)] = request.auth
  override val followRedirects: Option[Boolean] = request.followRedirects
  override val requestTimeout: Option[Int] = request.requestTimeout
  override val virtualHost = request.virtualHost
  override val proxyServer: Option[WSProxyServer] = request.proxyServer

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

  override def execute() = tracer.traceFuture(spanName) { data =>
    request.addHttpHeaders(tracer.toMap(data.span).toSeq: _*).execute()
  }(traceData)
  override def stream() = tracer.traceFuture(spanName) { data =>
    request.addHttpHeaders(tracer.toMap(data.span).toSeq: _*).stream()
  }(traceData)

  override def uri = request.uri
  override def contentType = request.contentType
  override def withBody[T](body: T)(implicit evidence$1: BodyWritable[T]) = new TracingWSRequest(spanName, request.withBody(body), tracer, traceData)

  override def patch(body: Source[Part[Source[ByteString, _]], _]) = withBody(body).execute("PATCH")
  override def patch[T](body: T)(implicit evidence$2: BodyWritable[T]) = withBody(body).execute("PATCH")
  override def patch(body: File) = withBody(body).execute("PATCH")

  override def get() = execute("GET")

  override def post[T](body: T)(implicit evidence$3: BodyWritable[T]) = withBody(body).execute("POST")
  override def post(body: File) = withBody(body).execute("POST")
  override def post(body: Source[Part[Source[ByteString, _]], _]) = withBody(body).execute("POST")

  override def put[T](body: T)(implicit evidence$4: BodyWritable[T]) = withBody(body).execute("PUT")
  override def put(body: File) = withBody(body).execute("PUT")
  override def put(body: Source[Part[Source[ByteString, _]], _]) = withBody(body).execute("PUT")

  override def delete() = execute("DELETE")
  override def head() = execute("HEAD")
  override def options() = execute("OPTIONS")
  override def execute(method: String) = withMethod(method).execute()

  override def cookies = request.cookies
  override def withHttpHeaders(headers: (String, String)*) = new TracingWSRequest(spanName, request.withHttpHeaders(headers: _*), tracer, traceData)
  override def withQueryStringParameters(parameters: (String, String)*) = {
    new TracingWSRequest(spanName, request.withQueryStringParameters(parameters: _*), tracer, traceData)
  }
  override def withCookies(cookies: WSCookie*) = new TracingWSRequest(spanName, request.withCookies(cookies: _*), tracer, traceData)
}
