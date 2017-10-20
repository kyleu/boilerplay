package models.audit

import java.time.LocalDateTime
import models.result.BaseResult
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.paging.PagingOptions

case class AuditRecordResult(
  override val filters: Seq[Filter] = Nil,
  override val orderBys: Seq[OrderBy] = Nil,
  override val totalCount: Int = 0,
  override val paging: PagingOptions = PagingOptions(),
  override val results: Seq[AuditRecord] = Nil,
  override val durationMs: Int = 0,
  override val occurred: LocalDateTime = util.DateUtils.now
) extends BaseResult[AuditRecord]

object AuditRecordResult {
  def fromRecords(
    q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int],
    startMs: Long, totalCount: Int, results: Seq[AuditRecord]
  ) = {
    val paging = PagingOptions.from(totalCount, limit, offset)
    val durationMs = (System.currentTimeMillis - startMs).toInt
    AuditRecordResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = totalCount, results = results, durationMs = durationMs)
  }
}
