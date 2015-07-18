package models.database

import com.github.mauricio.async.db.ResultSet
import services.database.Database

import scala.concurrent.Future

trait RawQuery[A] {
  def sql: String
  def values: Seq[Any] = Seq.empty
  def handle(results: ResultSet): A
  def apply(): Future[A] = Database.query(this)
}

trait Query[A] extends RawQuery[A] {
  def handle(results: ResultSet) = reduce(results.toIterator.map(rd => new Row(rd)))
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
