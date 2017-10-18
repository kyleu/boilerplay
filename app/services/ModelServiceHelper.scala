package services

import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import util.Logging
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

abstract class ModelServiceHelper[T](val key: String) extends Logging {
  def tracing: TracingService

  def traceF[A](name: String)(f: TraceData => Future[A])(implicit tracd: TraceData) = {
    tracing.trace(key + ".service." + name)(td => f(td))
  }
  def traceB[A](name: String)(f: TraceData => A)(implicit tracd: TraceData) = {
    tracing.traceBlocking(key + ".service." + name)(td => f(td))
  }

  def countAll(filters: Seq[Filter])(implicit trace: TraceData): Int
  def getAll(
    filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData): Seq[T]

  def getAllWithCount(
    filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = traceB("get.all.with.count") { td =>
    val result = getAll(filters, orderBys, limit, offset)(td)
    val count = countAll(filters)(td)
    count -> result
  }

  def searchCount(q: String, filters: Seq[Filter])(implicit trace: TraceData): Int
  def search(
    q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]
  )(implicit trace: TraceData): Seq[T]

  def searchWithCount(
    q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = traceB("search.with.count") { td =>
    val result = search(q, filters, orderBys, limit, offset)(td)
    val count = searchCount(q, filters)(td)
    count -> result
  }

  protected def fieldVal(fields: Seq[DataField], k: String) = fields.find(_.k == k).flatMap(_.v).getOrElse(util.NullUtils.str)
}
