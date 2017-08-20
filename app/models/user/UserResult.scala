package models.user

import java.time.LocalDateTime

import models.result.BaseResult
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.paging.PagingOptions

case class UserResult(
  override val filters: Seq[Filter] = Nil,
  override val orderBys: Seq[OrderBy] = Nil,
  override val totalCount: Int = 0,
  override val paging: PagingOptions = PagingOptions(),
  override val results: Seq[User] = Nil,
  override val durationMs: Int = 0,
  override val occurred: LocalDateTime = util.DateUtils.now
) extends BaseResult[User]

object UserResult {
  def fromRecords(
    q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int],
    startMs: Long, totalCount: Int, results: Seq[User]
  ) = {
    val paging = PagingOptions.from(totalCount, limit, offset)
    val durationMs = (System.currentTimeMillis - startMs).toInt
    UserResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = totalCount, results = results, durationMs = durationMs)
  }
}
