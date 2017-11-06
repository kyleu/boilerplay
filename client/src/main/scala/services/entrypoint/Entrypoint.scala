package services.entrypoint

import util.Logging

object Entrypoint {
  private[this] var initialized = false

  private def initIfNeeded(debug: Boolean) = if (!initialized) {
    Logging.init(debug)
    initialized = true
  }
}

abstract class Entrypoint(val key: String, val debug: Boolean) {
  Entrypoint.initIfNeeded(debug)
  Logging.info(s"${util.Config.projectName} started with entrypoint [$key].")
}
