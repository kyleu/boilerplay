package models.sandbox

import models.Application
import models.database.{Query, Row, Statement}
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.TraceData

object TableLogic {
  final private[this] case class CountRows(table: String) extends Query[Long] {
    override def name = "countRows"
    override def sql = s"select count(*) c from $table"
    override def reduce(rows: Iterator[Row]) = rows.toSeq.headOption.getOrElse(throw new IllegalStateException("No count.")).as[Long]("c")
  }

  final private[this] case class CopyRows(src: String, dest: String) extends Statement {
    override def name = "copyRows"
    override def sql = s"create table $dest as (select * from $src)"
  }

  final private[this] case class MoveRows(src: String, dest: String, cols: String) extends Statement {
    override def name = "moveRows"
    override def sql = s"insert into $dest ($cols) (select $cols from $src)"
  }

  def copyTable(app: Application, arg: String)(implicit trace: TraceData) = {
    val (src, dest) = arg.split('/').toList match {
      case s :: Nil => s -> (s + "COPY")
      case s :: d :: Nil => s -> d
      case _ => throw new IllegalStateException(s"Arg [$arg] should be of form [src/dest].")
    }
    ApplicationDatabase.queryF(CountRows(src)).flatMap { count =>
      ApplicationDatabase.executeF(CopyRows(src, dest)).map { _ =>
        s"Copied table [$src] with [${util.NumberUtils.withCommas(count)}] rows to table [$dest]."
      }
    }
  }

  def restoreTable(app: Application, arg: String)(implicit trace: TraceData) = {
    val (src, dest, cols) = arg.split('/').toList match {
      case s :: d :: c :: Nil => (s, d, c)
      case _ => throw new IllegalStateException(s"Arg [$arg] should be of form [src/dest/cols].")
    }
    ApplicationDatabase.queryF(CountRows(dest)).flatMap { count =>
      if (count != 0) {
        throw new IllegalStateException(s"Attempted to restore to table [$dest] with [$count] rows.")
      }
      ApplicationDatabase.executeF(MoveRows(src, dest, cols)).map { _ =>
        s"Copied table [$src] with [${util.NumberUtils.withCommas(count)}] rows to table [$dest]."
      }
    }
  }
}
