package models.queries

object EngineHelper {
  private[this] val mySqlLeftQuote = "`"
  private[this] val mySqlRightQuote = "`"
  private[this] val postgresLeftQuote = "\""
  private[this] val postgresRightQuote = "\""

  def quote(n: String, engine: String) = engine match {
    case "mysql" => mySqlLeftQuote + n + mySqlRightQuote
    case "postgres" | "postgresql" => postgresLeftQuote + n + postgresRightQuote
  }
}
