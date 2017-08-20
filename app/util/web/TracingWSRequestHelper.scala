package util.web

import java.io.File

import akka.stream.scaladsl.Source
import akka.util.ByteString
import play.api.libs.ws._
import play.api.mvc.MultipartFormData.Part

private trait TracingWSRequestHelper extends WSRequest { this: TracingWSRequest =>
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

  override def uri = request.uri
  override def contentType = request.contentType

  override def cookies = request.cookies

  override def withBody[T](body: T)(implicit evidence$1: BodyWritable[T]) = new TracingWSRequest(spanName, request.withBody(body), tracer, traceData)

  override def get() = execute("GET")

  override def patch(body: Source[Part[Source[ByteString, _]], _]) = withBody(body).execute("PATCH")
  override def patch[T](body: T)(implicit evidence$2: BodyWritable[T]) = withBody(body).execute("PATCH")
  override def patch(body: File) = withBody(body).execute("PATCH")

  override def post[T](body: T)(implicit evidence$3: BodyWritable[T]) = withBody(body).execute("POST")
  override def post(body: File) = withBody(body).execute("POST")
  override def post(body: Source[Part[Source[ByteString, _]], _]) = withBody(body).execute("POST")

  override def put[T](body: T)(implicit evidence$4: BodyWritable[T]) = withBody(body).execute("PUT")
  override def put(body: File) = withBody(body).execute("PUT")
  override def put(body: Source[Part[Source[ByteString, _]], _]) = withBody(body).execute("PUT")

  override def delete() = execute("DELETE")
  override def head() = execute("HEAD")
  override def options() = execute("OPTIONS")
}
