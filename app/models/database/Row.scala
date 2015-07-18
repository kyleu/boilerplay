package models.database

import com.github.mauricio.async.db.RowData

class Row(val rowData: RowData) {
  def asOpt[T](s: String) = Option(rowData(s)).map(_.asInstanceOf[T])

  def as[T](s: String) = Option(rowData(s)) match {
    case None => throw new IllegalArgumentException(s"Column [$s] is null.")
    case Some(x) => x.asInstanceOf[T]
  }
}
