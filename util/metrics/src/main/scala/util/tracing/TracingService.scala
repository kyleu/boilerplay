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
  implicit val executionContext: ExecutionContext = actorSystem.dispatchers.lookup("context.tracing")

  def noopTrace[A](name: String)(f: TraceData => Future[A]) = f(TraceData(newSpan(Map.empty[String, String])((headers, key) => headers.get(key)).name("noop")))
  def topLevelTrace[A](name: String)(f: TraceData => Future[A]) = {
    val span = serverReceived(spanName = name, span = newSpan(Map.empty[String, String])((headers, key) => headers.get(key)))
    span.tag("top.level", "true")

    val result = f(TraceData(span))
    result.onComplete {
      case Failure(t) => serverSend(span, "failed" -> s"Finished with exception: ${t.getMessage}")
      case Success(_) => serverSend(span)
    }
    result
  }

  private[this] val sender = OkHttpSender.json(s"http://${cnf.tracingServer}:${cnf.tracingPort}/api/v1/spans")
  private[this] val reporter = AsyncReporter.v2(sender)
  private[this] val samp = Sampler.create(cnf.tracingSampleRate)
  private[this] val ctx = MDCCurrentTraceContext.create()
  val tracing = Tracing.newBuilder().localServiceName(cnf.tracingService).spanReporter(reporter).currentTraceContext(ctx).traceId128Bit(true).sampler(samp).build()
  tracing.setNoop(!cnf.tracingEnabled)
  private[this] val tracer: Tracer = tracing.tracer
  log.info(s"Tracing enabled, sending results to [${cnf.tracingServer}:${cnf.tracingPort}@${cnf.tracingService}] using sample rate [${cnf.tracingSampleRate}].")

  def newServerSpan(traceName: String, tags: (String, String)*)(implicit parentData: TraceData) = {
    val childSpan = tracer.newChild(parentData.span.context()).name(traceName).kind(Span.Kind.SERVER)
    tags.foreach { case (key, value) => childSpan.tag(key, value) }
    childSpan.start().tag("thread.id", Thread.currentThread.getName)
  }

  def traceBlocking[A](traceName: String, tags: (String, String)*)(f: TraceData => A)(implicit parentData: TraceData) = {
    val childSpan = newServerSpan(traceName, tags: _*)
    Try(f(TraceData(childSpan))) match {
      case Success(result) =>
        childSpan.finish()
        result
      case Failure(t) =>
        childSpan.tag("error.type", t.getClass.getSimpleName.stripSuffix("$"))
        childSpan.tag("error.message", t.getMessage)
        childSpan.finish()
        throw t
    }
  }

  def trace[A](traceName: String, tags: (String, String)*)(f: TraceData => Future[A])(implicit parentData: TraceData) = {
    val childSpan = newServerSpan(traceName, tags: _*)
    val result = f(TraceData(childSpan))
    result.onComplete {
      case Failure(t) =>
        childSpan.tag("error.type", t.getClass.getSimpleName.stripSuffix("$"))
        childSpan.tag("error.message", t.getMessage)
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
