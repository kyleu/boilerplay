package models.entrypoint

import util.Logging

object Entrypoint {
  private[this] var initialized = false

  private def initIfNeeded() = if (!initialized) {
    Logging.init()
    initialized = true
  }
}

abstract class Entrypoint(val key: String) {
  Entrypoint.initIfNeeded()
  Logging.info(s"${util.Config.projectName} started with entrypoint [$key].")
}
