package util

import org.scalajs.dom
import org.scalajs.dom.raw.Event
import scribe.Logging

object LoggingErrorHandler extends Logging {
  private[this] var initialized = false

  def install() = if (!initialized) {
    initialized = true
    dom.window.onerror = (e: Event, source: String, lineno: Int, colno: Int) => {
      logger.info(s"Script error [$e] encountered in [$source:$lineno:$colno]")
      logger.error(e.toString)
    }
  }
}
