package services

import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import util.Logging
import util.FutureUtils.databaseContext

import scala.concurrent.Future

trait ModelServiceHelper[T] extends Logging {
  def logChanges(ids: Seq[(String, Any)], updates: Seq[DataField]) = {}

  def countAll(filters: Seq[Filter]): Future[Int]
  def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None): Future[Seq[T]]

  def getAllWithCount(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None) = {
    val result = getAll(filters, orderBys, limit, offset)
    val count = countAll(filters)
    result.flatMap(r => count.map(_ -> r))
  }

  def searchCount(q: String, filters: Seq[Filter]): Future[Int]
  def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]): Future[Seq[T]]

  def searchWithCount(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int] = None, offset: Option[Int] = None) = {
    val result = search(q, filters, orderBys, limit, offset)
    val count = searchCount(q, filters)
    result.flatMap(r => count.map(_ -> r))
  }
}
