package models.database

trait Statement {
  def name: String
  def sql: String
  def values: Seq[Any] = Seq.empty
}
