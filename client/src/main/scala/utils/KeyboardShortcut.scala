package utils

import java.util.UUID

import enumeratum._

sealed abstract class KeyboardShortcut(val pattern: String, val key: String, val call: (Option[UUID]) => Unit, val isGlobal: Boolean = true) extends EnumEntry

object KeyboardShortcut extends Enum[KeyboardShortcut] {
  case object Help extends KeyboardShortcut("?", "help", { _ =>
    utils.Logging.info("Help!")
  })

  override val values = findValues
}
