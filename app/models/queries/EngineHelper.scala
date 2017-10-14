package models.queries

import java.time.{LocalDate, LocalDateTime, LocalTime}

object EngineHelper {
  private[this] val mySqlLeftQuote = "`"
  private[this] val mySqlRightQuote = "`"
  private[this] val postgresLeftQuote = "\""
  private[this] val postgresRightQuote = "\""

  def quote(n: String, engine: String) = engine match {
    case "mysql" => mySqlLeftQuote + n + mySqlRightQuote
    case "postgres" | "postgresql" => postgresLeftQuote + n + postgresRightQuote
  }

  def toDatabaseFormat(v: Any) = v match {
    case x: LocalDateTime => java.sql.Timestamp.valueOf(x)
    case x: LocalDate => java.sql.Date.valueOf(x)
    case x: LocalTime => java.sql.Time.valueOf(x)
    case x => x
  }
}
