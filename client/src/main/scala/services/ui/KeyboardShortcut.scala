package services.ui

import java.util.UUID

import enumeratum._
import services.Logging

sealed abstract class KeyboardShortcut(val pattern: String, val key: String, val call: (Option[UUID]) => Unit, val isGlobal: Boolean = true) extends EnumEntry

object KeyboardShortcut extends Enum[KeyboardShortcut] with CirceEnum[KeyboardShortcut] {
  case object Help extends KeyboardShortcut("?", "help", { _ =>
    Logging.info("Help!")
  })

  override val values = findValues
}
