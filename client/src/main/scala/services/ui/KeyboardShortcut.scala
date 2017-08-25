package services.ui

import java.util.UUID

import enumeratum._
import scribe.Logging

sealed abstract class KeyboardShortcut(val pattern: String, val key: String, val call: (Option[UUID]) => Unit, val isGlobal: Boolean = true) extends EnumEntry

object KeyboardShortcut extends Enum[KeyboardShortcut] with CirceEnum[KeyboardShortcut] with Logging {
  case object Help extends KeyboardShortcut("?", "help", { _ =>
    logger.info("Help!")
  })

  override val values = findValues
}
