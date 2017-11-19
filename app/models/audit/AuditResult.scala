/* Generated File */
package models.audit

import java.time.LocalDateTime

import io.circe.{Decoder, Encoder}
import models.result.BaseResult
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.paging.PagingOptions
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._

case class AuditResult(
    override val filters: Seq[Filter] = Nil,
    override val orderBys: Seq[OrderBy] = Nil,
    override val totalCount: Int = 0,
    override val paging: PagingOptions = PagingOptions(),
    override val results: Seq[Audit] = Nil,
    override val durationMs: Int = 0,
    override val occurred: LocalDateTime = util.DateUtils.now
) extends BaseResult[Audit]

object AuditResult {
  implicit val jsonEncoder: Encoder[AuditResult] = deriveEncoder
  implicit val jsonDecoder: Decoder[AuditResult] = deriveDecoder

  def fromRecords(
    q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int],
    startMs: Long, totalCount: Int, results: Seq[Audit]
  ) = {
    val paging = PagingOptions.from(totalCount, limit, offset)
    val durationMs = (System.currentTimeMillis - startMs).toInt
    AuditResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = totalCount, results = results, durationMs = durationMs)
  }
}
