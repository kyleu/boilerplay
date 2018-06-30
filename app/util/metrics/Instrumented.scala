package util.metrics

import java.util.concurrent.TimeUnit

import io.micrometer.core.instrument.binder.jvm.{ClassLoaderMetrics, JvmGcMetrics, JvmMemoryMetrics, JvmThreadMetrics}
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.{PrometheusConfig, PrometheusMeterRegistry}
import util.Logging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.util.control.NonFatal

object Instrumented extends Logging {
  private[this] def cn(x: Any) = x.getClass.getSimpleName.replaceAllLiterally("$", "")

  private[this] var registry: Option[PrometheusMeterRegistry] = None

  def reg = registry.getOrElse(throw new IllegalStateException("Not started"))
  def regOpt = registry

  def start() = {
    val r = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    registry = Some(r)
    new ClassLoaderMetrics().bindTo(r)
    new JvmMemoryMetrics().bindTo(r)
    new JvmGcMetrics().bindTo(r)
    new ProcessorMetrics().bindTo(r)
    new JvmThreadMetrics().bindTo(r)
  }

  def stop() = {
    registry.foreach(_.getPrometheusRegistry.clear())
    registry.foreach(_.close())
    registry = None
  }

  def timeReceive[A](msg: Any, key: String, tags: String*)(f: => A) = registry.map { r =>
    val startNanos = System.nanoTime
    try {
      val ret = f
      r.timer(key, ("class" :: cn(msg) :: Nil) ++ tags: _*).record(System.nanoTime - startNanos, TimeUnit.NANOSECONDS)
      ret
    } catch {
      case NonFatal(x) =>
        r.timer(key, ("class" :: cn(msg) :: "error" :: cn(x) :: Nil) ++ tags: _*).record(System.nanoTime - startNanos, TimeUnit.NANOSECONDS)
        throw x
    }
  }.getOrElse(f)

  def timeFuture[A](key: String, tags: String*)(future: => Future[A])(implicit context: ExecutionContext): Future[A] = registry.map { r =>
    val startNanos = System.nanoTime
    val f = try {
      future
    } catch {
      case NonFatal(ex) =>
        throw ex
    }
    f.onComplete {
      case Success(s) => r.timer(key, ("class" :: cn(s) :: Nil) ++ tags: _*).record(System.nanoTime - startNanos, TimeUnit.NANOSECONDS)
      case Failure(x) => r.timer(key, ("error" :: cn(x) :: Nil) ++ tags: _*).record(System.nanoTime - startNanos, TimeUnit.NANOSECONDS)
    }
    f
  }.getOrElse(future)
}
