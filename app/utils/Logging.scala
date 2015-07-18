package utils

import _root_.play.api.Logger
import org.slf4j.LoggerFactory
import utils.metrics.Instrumented

object Logging extends Instrumented {
  val traceMeter = metrics.meter("log.trace")
  val debugMeter = metrics.meter("log.debug")
  val infoMeter = metrics.meter("log.info")
  val warnMeter = metrics.meter("log.warn")
  val errorMeter = metrics.meter("log.error")

  case class CustomLogger(name: String) extends Logger(LoggerFactory.getLogger(name)) {
    override def trace(message: => String) = {
      traceMeter.mark()
      super.trace(message)
    }
    override def trace(message: => String, error: => Throwable) = {
      traceMeter.mark()
      super.trace(message, error)
    }
    override def debug(message: => String) = {
      debugMeter.mark()
      super.debug(message)
    }
    override def debug(message: => String, error: => Throwable) = {
      debugMeter.mark()
      super.debug(message, error)
    }
    override def info(message: => String) = {
      infoMeter.mark()
      super.info(message)
    }
    override def info(message: => String, error: => Throwable) = {
      infoMeter.mark()
      super.info(message, error)
    }
    override def warn(message: => String) = {
      warnMeter.mark()
      super.warn(message)
    }
    override def warn(message: => String, error: => Throwable) = {
      warnMeter.mark()
      super.warn(message, error)
    }
    override def error(message: => String) = {
      errorMeter.mark()
      super.error(message)
    }
    override def error(message: => String, error: => Throwable) = {
      errorMeter.mark()
      super.error(message, error)
    }
    def errorThenThrow(message: => String) = {
      this.error(message)
      throw new IllegalStateException(message)
    }
    def errorThenThrow(message: => String, error: => Throwable) = {
      this.error(message, error)
      throw error
    }
  }
}

trait Logging {
  protected[this] val log = {
    val name = s"boilerplay.${this.getClass.getSimpleName.replace("$", "")}"
    Logging.CustomLogger(name)
  }
}
