package models.queries

object EngineHelper {
  private[this] val postgresLeftQuote = "\""
  private[this] val postgresRightQuote = "\""

  def quote(n: String) = postgresLeftQuote + n + postgresRightQuote
}
