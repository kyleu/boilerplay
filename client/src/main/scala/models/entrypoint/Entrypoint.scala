package models.entrypoint

import com.kyleu.projectile.util.Logging

object Entrypoint {
  private[this] var initialized = false

  private def initIfNeeded() = if (!initialized) {
    Logging.init()
    initialized = true
  }
}

abstract class Entrypoint(val key: String) {
  Entrypoint.initIfNeeded()
  Logging.info(s"Started with entrypoint [$key].")
}
