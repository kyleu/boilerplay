package models.database

import models.database.jdbc.JdbcRow

trait RawQuery[A] {
  def name: String
  def sql: String
  def values: Seq[Any] = Seq.empty
  def handle(results: java.sql.ResultSet): A
}

trait Query[A] extends RawQuery[A] {
  override def handle(results: java.sql.ResultSet): A = reduce(new JdbcRow.Iter(results))
  def reduce(rows: Iterator[Row]): A
}

trait SingleRowQuery[A] extends Query[A] {
  def map(row: Row): A
  override final def reduce(rows: Iterator[Row]) = if (rows.hasNext) {
    rows.map(map).next()
  } else {
    throw new IllegalStateException(s"No row returned for [$sql].")
  }
}

trait FlatSingleRowQuery[A] extends Query[Option[A]] {
  def flatMap(row: Row): Option[A]
  override final def reduce(rows: Iterator[Row]) = if (rows.hasNext) { flatMap(rows.next()) } else { None }
}
