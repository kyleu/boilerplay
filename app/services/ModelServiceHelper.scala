package services

import models.result.filter.Filter
import models.result.orderBy.OrderBy
import util.Logging
import util.FutureUtils.databaseContext
import util.tracing.{TraceData, TracingService}
import zipkin.Endpoint

import scala.concurrent.Future

trait ModelServiceHelper[T] extends Logging {
  def tracing: TracingService

  private[this] lazy val endpoint = Endpoint.builder().serviceName(util.FormatUtils.classToPeriodDelimited(getClass)).build()

  def traceF[A](k: String)(f: TraceData => Future[A])(implicit traceData: TraceData) = tracing.traceFuture(k) { tn =>
    tn.span.remoteEndpoint(endpoint)
    f(tn)
  }

  def countAll(filters: Seq[Filter])(implicit trace: TraceData): Future[Int]
  def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData): Future[Seq[T]]

  def getAllWithCount(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all.with.count") { _ =>
      val result = getAll(filters, orderBys, limit, offset)
      val count = countAll(filters)
      result.flatMap(r => count.map(_ -> r))
    }
  }

  def searchCount(q: String, filters: Seq[Filter])(implicit trace: TraceData): Future[Int]
  def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData): Future[Seq[T]]

  def searchWithCount(
    q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = traceF("search.with.count") { _ =>
    val result = search(q, filters, orderBys, limit, offset)
    val count = searchCount(q, filters)
    result.flatMap(r => count.map(_ -> r))
  }
}
