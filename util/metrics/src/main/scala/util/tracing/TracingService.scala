package util.tracing

import akka.actor.ActorSystem
import brave.context.slf4j.MDCCurrentTraceContext
import brave.sampler.Sampler
import brave.{Span, Tracing}
import play.api.mvc.{AnyContent, Request, Result}
import util.Logging
import zipkin.reporter.AsyncReporter
import zipkin.reporter.okhttp3.OkHttpSender
import zipkin.{Endpoint, TraceKeys}

import scala.concurrent.ExecutionContext

case class TracingService(
    enabled: Boolean = true, actorSystem: ActorSystem, server: String = "localhost", port: Int = 9411, service: String = "apptest"
) extends Logging {

  implicit val executionContext: ExecutionContext = actorSystem.dispatchers.lookup(TracingKeys.contextKey)

  val (reporter, tracing, tracer) = if (enabled) {
    val sender = OkHttpSender.create(s"http://$server:$port/api/v1/spans")
    log.info(s"Tracing enabled, sending results to [$server:$port@$service].")
    val r = AsyncReporter.create(sender)
    val samp = Sampler.create(1.0f)
    val t = Tracing.newBuilder()
      .localServiceName(service)
      .reporter(r)
      .currentTraceContext(MDCCurrentTraceContext.create())
      .traceId128Bit(true)
      .sampler(samp)
      .build()
    (Some(r), Some(t), Some(t.tracer()))
  } else {
    log.info(s"Tracing disabled ignoring results.")

    (None, None, None)
  }

  def traceForRequest(controller: String, r: Request[AnyContent]) = tracer.map { t =>
    val trace = t.newTrace()
    trace.tag(TraceKeys.HTTP_PATH, r.path)
    trace.tag(TraceKeys.HTTP_METHOD, r.method)
    trace.tag(TraceKeys.HTTP_HOST, r.host)
    trace.tag(TraceKeys.HTTP_REQUEST_SIZE, r.body.asRaw.size.toString)
    trace.remoteEndpoint(Endpoint.builder().serviceName(controller).ipv4(127 << 24 | 1).port(1234).build())
    trace.start()
  }

  def completeForResult(trace: Option[Span], result: Result) = trace.foreach { span =>
    span.tag(TraceKeys.HTTP_STATUS_CODE, result.header.status.toString)
    result.body.contentLength.foreach { size =>
      span.tag(TraceKeys.HTTP_RESPONSE_SIZE, size.toString)
    }
    span.finish()
  }

  def failed(trace: Option[Span], ex: Throwable) = trace.foreach { span =>
    span.tag("exception", ex.toString)
    span.finish()
  }

  def close() = {
    tracing.foreach(_.close())
    reporter.foreach(_.close())
  }
}
