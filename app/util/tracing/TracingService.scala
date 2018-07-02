package util.tracing

import akka.actor.ActorSystem
import io.jaegertracing.Configuration
import io.jaegertracing.micrometer.MicrometerMetricsFactory
import io.opentracing.noop.NoopTracerFactory
import io.opentracing.propagation.{Format, TextMapExtractAdapter, TextMapInjectAdapter}
import io.opentracing.{Span, Tracer}
import util.Logging
import util.metrics.MetricsConfig

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

@javax.inject.Singleton
class TracingService @javax.inject.Inject() (actorSystem: ActorSystem, cnf: MetricsConfig) extends Logging {
  implicit val executionContext: ExecutionContext = actorSystem.dispatchers.lookup("context.tracing")

  def noopTrace[A](name: String)(f: TraceData => Future[A]) = {
    f(new TraceData())
  }
  def topLevelTrace[A](name: String)(f: TraceData => Future[A]) = {
    val span = tracer.buildSpan(name).start()
    span.setTag("top.level", "true")

    val result = f(TraceDataOpenTracing(span))
    result.onComplete {
      case Failure(t) => serverSend(span, "failed" -> s"Finished with exception: ${t.getMessage}")
      case Success(_) => serverSend(span)
    }
    result
  }
  def topLevelTraceBlocking[A](name: String)(f: TraceData => A): A = {
    val span = tracer.buildSpan(name).start()
    span.setTag("top.level", "true")
    try {
      val result = f(TraceDataOpenTracing(span))
      serverSend(span)
      result
    } catch {
      case NonFatal(x) =>
        serverSend(span, "failed" -> s"Finished with exception: ${x.getMessage}")
        throw x
    }
  }

  private[this] val tracer: Tracer = if (cnf.tracingEnabled) {
    val sampler = new Configuration.SamplerConfiguration().withType("const").withParam(1)
    val sender = new Configuration.SenderConfiguration().withAgentHost(cnf.tracingServer).withAgentPort(cnf.tracingPort)
    val reporter = new Configuration.ReporterConfiguration().withLogSpans(false).withSender(sender).withFlushInterval(1000).withMaxQueueSize(10000)
    val metrics = new MicrometerMetricsFactory()

    val loc = s"${cnf.tracingServer}:${cnf.tracingPort}@${util.Config.projectId}"
    log.info(s"Tracing enabled, sending results to [$loc] using sample rate [${cnf.tracingSampleRate}].")
    new Configuration(util.Config.projectId).withSampler(sampler).withReporter(reporter).withMetricsFactory(metrics).getTracer
  } else {
    NoopTracerFactory.create()
  }

  private[this] def newServerSpan(traceName: String, tags: (String, String)*)(implicit parentData: TraceData) = parentData match {
    case td: TraceDataOpenTracing =>
      val childSpan = tracer.buildSpan(traceName).asChildOf(td.span).start()
      tags.foreach { case (key, value) => childSpan.setTag(key, value) }
      Some(childSpan.setTag("thread.id", Thread.currentThread.getName))
    case _ => None
  }

  def traceBlocking[A](traceName: String, tags: (String, String)*)(f: TraceData => A)(implicit parentData: TraceData) = parentData match {
    case _: TraceDataOpenTracing => newServerSpan(traceName, tags: _*).map { childSpan =>
      Try(f(TraceDataOpenTracing(childSpan))) match {
        case Success(result) =>
          childSpan.finish()
          result
        case Failure(t) =>
          childSpan.setTag("error.type", t.getClass.getSimpleName.stripSuffix("$"))
          childSpan.setTag("error.message", t.getMessage)
          childSpan.finish()
          throw t
      }
    }.getOrElse(f(parentData))
    case _ => f(parentData)
  }

  def trace[A](traceName: String, tags: (String, String)*)(f: TraceData => Future[A])(implicit parentData: TraceData) = {
    val childSpan = newServerSpan(traceName, tags: _*)
    val result = f(childSpan.map(TraceDataOpenTracing.apply).getOrElse(parentData))
    result.onComplete[Unit] {
      case Failure(t) =>
        childSpan.foreach(_.setTag("error.type", t.getClass.getSimpleName.stripSuffix("$")))
        childSpan.foreach(_.setTag("error.message", t.getMessage))
      case _ => childSpan.foreach(_.finish())
    }
    result
  }

  def serverReceived(spanName: String, span: Span) = tracer.buildSpan(spanName).asChildOf(span).start()
  def serverSend(span: Span, tags: (String, String)*) = {
    tags.foreach { case (key, value) => span.setTag(key, value) }
    span.finish()
    span
  }

  def newSpan[A](name: String, headers: Map[String, String]) = {
    Option(tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headers.asJava))) match {
      case Some(extracted) => tracer.buildSpan(name).asChildOf(extracted)
      case None => tracer.buildSpan(name)
    }
  }

  def toMap(td: TraceData) = td match {
    case tdz: TraceDataOpenTracing =>
      val data = new java.util.HashMap[String, String]()
      tracer.inject(tdz.span.context, Format.Builtin.HTTP_HEADERS, new TextMapInjectAdapter(data))
      data.asScala
    case _ => Map.empty
  }

  def close() = {
  }
}
