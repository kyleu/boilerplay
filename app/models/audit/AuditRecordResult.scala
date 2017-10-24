/* Generated File */
package models.audit

import java.time.LocalDateTime

import io.circe.{Decoder, Encoder}
import models.result.BaseResult
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.paging.PagingOptions
import io.circe.generic.semiauto._
import io.circe.java8.time._

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
  implicit val jsonEncoder: Encoder[AuditRecordResult] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditRecordResult] = deriveDecoder

  def fromRecords(
    q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int],
    startMs: Long, totalCount: Int, results: Seq[AuditRecord]
  ) = {
    val paging = PagingOptions.from(totalCount, limit, offset)
    val durationMs = (System.currentTimeMillis - startMs).toInt
    AuditRecordResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = totalCount, results = results, durationMs = durationMs)
  }
}
