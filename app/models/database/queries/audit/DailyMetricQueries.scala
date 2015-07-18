package models.database.queries.audit

import models.audit.DailyMetric
import models.database.queries.BaseQueries
import models.database.{ FlatSingleRowQuery, Query, Row, SingleRowQuery, Statement }
import org.joda.time.{ LocalDate, LocalDateTime }

object DailyMetricQueries extends BaseQueries[DailyMetric] {
  override protected val tableName = "daily_metrics"
  override protected val columns = Seq("day", "metric", "value", "measured")
  override protected val idColumns = Seq("day", "metric")
  override protected val searchColumns = Seq("day", "metric", "value")

  case class GetValue(d: LocalDate, m: DailyMetric.Metric) extends FlatSingleRowQuery[Long] {
    override val sql = s"select value from $tableName where day = ? and metric = ?"
    override def values = Seq(d, m)
    override def flatMap(row: Row) = Some(row.as[Long]("value"))
  }
  val insert = Insert
  val insertBatch = InsertBatch

  case class GetMetrics(d: LocalDate) extends Query[Map[DailyMetric.Metric, Long]] {
    override def sql = s"select metric, value from $tableName where day = ?"
    override def values = Seq(d)
    override def reduce(rows: Iterator[Row]) = rows.map(tupleFromRow).toMap
  }

  case object GetAllMetrics extends Query[Seq[(LocalDate, Map[DailyMetric.Metric, Long])]] {
    import utils.DateUtils.localDateOrdering

    override def sql = s"select day, metric, value from $tableName order by day, metric"
    override def reduce(rows: Iterator[Row]) = rows.map { row =>
      (row.as[LocalDate]("day"), row.as[String]("metric"), row.as[Long]("value"))
    }.toSeq.groupBy(_._1).map { x =>
      x._1 -> x._2.map(y => DailyMetric.fromString(y._2) -> y._3).toMap
    }.toSeq.sortBy(_._1).reverse
  }

  case class GetTotals(last: LocalDate) extends Query[Map[DailyMetric.Metric, Long]] {
    override def sql = s"select metric, sum(value) as value from $tableName where day <= ? group by metric"
    override def values = Seq(last)
    override def reduce(rows: Iterator[Row]) = rows.map(tupleFromRow).toMap
  }

  case class RemoveByDay(d: LocalDate) extends Statement {
    override def sql = s"delete from $tableName where day = ?"
    override def values = Seq(d)
  }

  case class GetMetricHistory(metric: DailyMetric.Metric) extends Query[Seq[(LocalDate, Long)]] {
    override def sql = s"select day, value from $tableName where metric = ? order by day"
    override def values = Seq(metric)
    override def reduce(rows: Iterator[Row]) = rows.map(row => row.as[LocalDate]("day") -> row.as[Long]("value")).toSeq
  }

  case class UpdateMetric(dm: DailyMetric) extends Statement {
    override def sql: String = updateSql(Seq("value", "measured"))
    override def values = Seq(dm.value, dm.measured, dm.date, dm.metric)
  }

  case class CalculateMetric(metric: DailyMetric.Metric, override val sql: String, d: LocalDate) extends SingleRowQuery[(DailyMetric.Metric, Long)] {
    override val values = sql.count(_ == '?') match {
      case 0 => Nil
      case 1 => Seq(s"${d.plusDays(1).toString} 00:00:00")
      case 2 => Seq(s"${d.toString} 00:00:00", s"${d.plusDays(1).toString} 00:00:00")
    }
    override def map(row: Row) = metric -> row.as[Long]("c")
  }

  private[this] def tupleFromRow(row: Row) = DailyMetric.fromString(row.as[String]("metric")) -> row.as[Long]("value")

  override protected def fromRow(row: Row) = {
    val day = row.as[LocalDate]("day")
    val metric = DailyMetric.fromString(row.as[String]("metric"))
    val value = row.as[Long]("value")
    val measured = row.as[LocalDateTime]("measured")
    DailyMetric(day, metric, value, measured)
  }

  override protected def toDataSeq(m: DailyMetric) = Seq(m.date, m.metric, m.value, m.measured)
}
