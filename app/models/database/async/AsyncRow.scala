package models.database.async

import com.github.mauricio.async.db.RowData
import models.database.Row

class AsyncRow(val rowData: RowData) extends Row {
  override def asOpt[T](s: String) = Option(rowData(s)).map(_.asInstanceOf[T])

  override def as[T](s: String) = Option(rowData(s)) match {
    case None => throw new IllegalArgumentException(s"Column [$s] is null.")
    case Some(x) => x.asInstanceOf[T]
  }
}
