package services

import models.result.filter.Filter
import models.result.orderBy.OrderBy
import util.Logging
import util.FutureUtils.databaseContext
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

trait ModelServiceHelper[T] extends Logging {
  def tracing: TracingService

  def traceF[A](k: String)(f: TraceData => Future[A])(implicit traceData: TraceData) = tracing.traceFuture(k)(f)

  def countAll(filters: Seq[Filter])(implicit trace: TraceData): Future[Int]
  def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData): Future[Seq[T]]

  def getAllWithCount(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    tracing.traceFuture("getAllWithCount") { _ =>
      val result = getAll(filters, orderBys, limit, offset)
      val count = countAll(filters)
      result.flatMap(r => count.map(_ -> r))
    }
  }

  def searchCount(q: String, filters: Seq[Filter])(implicit trace: TraceData): Future[Int]
  def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData): Future[Seq[T]]

  def searchWithCount(
    q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = tracing.traceFuture("searchWithCount") { _ =>
    val result = search(q, filters, orderBys, limit, offset)
    val count = searchCount(q, filters)
    result.flatMap(r => count.map(_ -> r))
  }
}
