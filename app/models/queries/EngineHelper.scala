package models.queries

import java.time.{LocalDate, LocalDateTime, LocalTime}

import util.JodaDateUtils

object EngineHelper extends JodaDateUtils {
  protected val leftQuote = "\""
  protected val rightQuote = "\""

  def quote(n: String) = leftQuote + n + rightQuote

  def toDatabaseFormat(v: Any) = v match {
    case x: LocalDateTime => toJoda(x)
    case x: LocalDate => toJoda(x)
    case x: LocalTime => toJoda(x)
    case x => x
  }
}
