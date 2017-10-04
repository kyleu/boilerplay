package models.queries

import java.time.{LocalDate, LocalDateTime, LocalTime}

object EngineHelper {
  protected val leftQuote = "\""
  protected val rightQuote = "\""

  def quote(n: String) = leftQuote + n + rightQuote

  def toDatabaseFormat(v: Any) = v match {
    case x: LocalDateTime => java.sql.Timestamp.valueOf(x)
    case x: LocalDate => java.sql.Date.valueOf(x)
    case x: LocalTime => java.sql.Time.valueOf(x)
    case x => x
  }
}
