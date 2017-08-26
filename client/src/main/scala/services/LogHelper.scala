package services

import org.scalajs.dom
import org.scalajs.dom.raw.Event
import scribe.formatter.FormatterBuilder
import scribe.{Level, LogHandler, Logger, Logging}

object LogHelper extends Logging {
  private[this] var initialized = false

  private[this] val format = FormatterBuilder().date().string(" ").level.string(" ").positionAbbreviated.newLine.string(" - ").message.newLine

  private[this] def handler(l: Level) = LogHandler(level = l, formatter = format)

  def init(debug: Boolean) = {
    if (initialized) {
      throw new IllegalStateException("Logging initialized twice!")
    }
    Logger.root.clearHandlers()

    val h = if (debug) { handler(Level.Debug) } else { handler(Level.Info) }
    Logger.root.addHandler(h)

    installErrorHandler()

    initialized = true
  }

  private[this] def installErrorHandler() = if (!initialized) {
    initialized = true
    dom.window.onerror = (e: Event, source: String, lineno: Int, colno: Int) => {
      logger.info(s"Script error [$e] encountered in [$source:$lineno:$colno]")
      logger.error(e.toString)
    }
  }
}
