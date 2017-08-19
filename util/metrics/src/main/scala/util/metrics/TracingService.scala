package util.metrics

import brave.Tracing
import brave.context.slf4j.MDCCurrentTraceContext
import util.Logging
import zipkin.reporter.AsyncReporter
import zipkin.reporter.okhttp3.OkHttpSender

case class TracingService(enabled: Boolean = true, server: String = "localhost", port: Int = 9411, service: String = "apptest") extends Logging {
  private[this] val sender = OkHttpSender.create(s"http://$server:$port/api/v1/spans")
  private[this] val reporter = AsyncReporter.create(sender)

  private[this] val tracing = if (enabled) {
    log.info(s"Tracing enabled, sending results to [$server:$port@$service].")
    Tracing.newBuilder().localServiceName(service).reporter(reporter).currentTraceContext(MDCCurrentTraceContext.create()).build()
  } else {
    log.info(s"Tracing disabled (but actually not really), ignoring results.")
    Tracing.newBuilder().build()
  }

  val tracer = tracing.tracer()

  def test() = {
    val span = tracer.newTrace().name("encode").start()
    log.info("Trace test complete.")
    span.finish()
  }

  def close() = {
    tracing.close()
    reporter.close()
  }
}
