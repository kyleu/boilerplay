package util

import org.slf4j.{LoggerFactory, MarkerFactory}
import play.api.{Logger, MarkerContext}
import util.metrics.Instrumented

object Logging {
  private[this] val metricsId = util.Config.metricsId + "_logging"

  final case class CustomLogger(name: String) extends Logger(LoggerFactory.getLogger(name)) {
    implicit val mc: MarkerContext = MarkerContext(MarkerFactory.getMarker(name))

    override def trace(message: => String)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "trace").increment())
      super.trace(message)
    }
    override def trace(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "trace").increment())
      super.trace(message, error)
    }
    override def debug(message: => String)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "debug").increment())
      super.debug(message)
    }
    override def debug(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "debug").increment())
      super.debug(message, error)
    }
    override def info(message: => String)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "info").increment())
      super.info(message)
    }
    override def info(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "info").increment())
      super.info(message, error)
    }
    override def warn(message: => String)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "warn").increment())
      super.warn(message)
    }
    override def warn(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "warn").increment())
      super.warn(message, error)
    }
    override def error(message: => String)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "error").increment())
      super.error(message)
    }
    override def error(message: => String, error: => Throwable)(implicit mc: play.api.MarkerContext) = {
      Instrumented.regOpt.foreach(_.counter(metricsId, "level", "error").increment())
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
