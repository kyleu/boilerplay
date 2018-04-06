package util

import io.prometheus.client.Gauge
import org.slf4j.{LoggerFactory, MarkerFactory}
import play.api.{Logger, MarkerContext}

object Logging {
  private[this] lazy val gauge = Gauge.build(util.Config.projectId + "_logging", "Logging statistics.").labelNames("level").register()

  final case class CustomLogger(name: String) extends Logger(LoggerFactory.getLogger(name)) {
    implicit val mc: MarkerContext = MarkerContext(MarkerFactory.getMarker(name))

    override def trace(message: => String)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("trace").inc()
      super.trace(message)
    }
    override def trace(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("trace").inc()
      super.trace(message, error)
    }
    override def debug(message: => String)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("debug").inc()
      super.debug(message)
    }
    override def debug(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("debug").inc()
      super.debug(message, error)
    }
    override def info(message: => String)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("info").inc()
      super.info(message)
    }
    override def info(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("info").inc()
      super.info(message, error)
    }
    override def warn(message: => String)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("warn").inc()
      super.warn(message)
    }
    override def warn(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("warn").inc()
      super.warn(message, error)
    }
    override def error(message: => String)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("error").inc()
      super.error(message)
    }
    override def error(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      gauge.labels("error").inc()
      super.error(message, error)
    }
    def errorThenThrow(message: => String)(implicit mc: play.api.MarkerContext) = {
      this.error(message)
      throw new IllegalStateException(message)
    }
    def errorThenThrow(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      this.error(message, error)
      throw error
    }
  }
}

trait Logging {
  protected[this] val log = Logging.CustomLogger(s"${util.Config.projectId}.${this.getClass.getSimpleName.replace("$", "")}")
}
