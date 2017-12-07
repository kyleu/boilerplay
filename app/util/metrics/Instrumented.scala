package util.metrics

import io.prometheus.client.Histogram
import io.prometheus.client.exporter.HTTPServer
import io.prometheus.client.hotspot.DefaultExports
import util.Logging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

object Instrumented extends Logging {
  private[this] var server: Option[HTTPServer] = None

  def start(port: Int) = {
    log.info(s"Exposing Prometheus metrics on port [$port].")
    server = Some(new HTTPServer(port))
    DefaultExports.initialize()
  }

  def stop() = {
    server.foreach(_.stop())
  }

  def timeFuture[A](metric: Histogram, labels: String*)(future: => Future[A])(implicit context: ExecutionContext): Future[A] = {
    val ctx = metric.labels(labels: _*).startTimer()
    val f = try {
      future
    } catch {
      case NonFatal(ex) =>
        ctx.close()
        throw ex
    }
    f.onComplete(_ => ctx.close())
    f
  }
}
