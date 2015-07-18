package models.database

trait Statement {
  def sql: String
  def values: Seq[Any] = Seq.empty
}
