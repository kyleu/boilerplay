package util.tracing

import akka.actor.ActorSystem
import brave.context.slf4j.MDCCurrentTraceContext
import brave.sampler.Sampler
import brave.{Span, Tracer, Tracing}
import util.Logging
import util.metrics.MetricsConfig
import zipkin.reporter.AsyncReporter
import zipkin.reporter.okhttp3.OkHttpSender

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

@javax.inject.Singleton
class TracingService @javax.inject.Inject() (actorSystem: ActorSystem, cnf: MetricsConfig) extends Logging {
  implicit val executionContext: ExecutionContext = actorSystem.dispatchers.lookup(TracingKeys.contextKey)

  private[this] val sender = OkHttpSender.create(s"http://${cnf.tracingServer}:${cnf.tracingPort}/api/v1/spans")
  private[this] val reporter = AsyncReporter.create(sender)
  private[this] val samp = Sampler.create(cnf.tracingSampleRate)

  private[this] val ctx = MDCCurrentTraceContext.create()
  val tracing = Tracing.newBuilder().localServiceName(cnf.tracingService).reporter(reporter).currentTraceContext(ctx).traceId128Bit(true).sampler(samp).build()
  tracing.setNoop(!cnf.tracingEnabled)

  val tracer: Tracer = tracing.tracer

  log.info(s"Tracing enabled, sending results to [${cnf.tracingServer}:${cnf.tracingPort}@${cnf.tracingService}] using sample rate [${cnf.tracingSampleRate}].")

  def trace[A](traceName: String, tags: (String, String)*)(f: TraceData => A)(implicit parentData: TraceData) = {
    val childSpan = tracer.newChild(parentData.span.context()).name(traceName).kind(Span.Kind.CLIENT)
    tags.foreach { case (key, value) => childSpan.tag(key, value) }
    childSpan.start()
    Try(f(TraceData(childSpan))) match {
      case Success(result) =>
        childSpan.finish()
        result
      case Failure(t) =>
        childSpan.tag("failed", s"Finished with [${t.getClass.getSimpleName}] exception: [${t.getMessage}].")
        childSpan.finish()
        throw t
    }
  }

  def traceFuture[A](traceName: String, tags: (String, String)*)(f: TraceData => Future[A])(implicit parentData: TraceData) = {
    val childSpan = tracer.newChild(parentData.span.context()).name(traceName).kind(Span.Kind.CLIENT)
    tags.foreach { case (key, value) => childSpan.tag(key, value) }
    childSpan.start()
    val result = f(TraceData(childSpan))
    result.onComplete {
      case Failure(t) => childSpan.tag("failed", s"Finished with exception: ${t.getMessage}").finish()
      case _ => childSpan.finish()
    }
    result
  }

  def serverReceived(spanName: String, span: Span) = span.name(spanName).kind(Span.Kind.SERVER).start()

  def serverSend(span: Span, tags: (String, String)*) = {
    tags.foreach { case (key, value) => span.tag(key, value) }
    span.finish()
    span
  }

  def newSpan[A](headers: A)(getHeader: (A, String) => Option[String]) = {
    val contextOrFlags = tracing.propagation().extractor(
      (carrier: A, key: String) => getHeader(carrier, key).orNull
    ).extract(headers)
    Option(contextOrFlags.context()).map(tracer.newChild).getOrElse(tracer.newTrace(contextOrFlags.samplingFlags()))
  }

  def toMap(span: Span) = {
    val data = collection.mutable.Map[String, String]()
    tracing.propagation().injector(
      (carrier: collection.mutable.Map[String, String], key: String, value: String) => carrier += key -> value
    ).inject(span.context(), data)
    data.toMap
  }

  def close() = {
    tracing.close()
    reporter.close()
  }
}
