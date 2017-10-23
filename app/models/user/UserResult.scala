/* Generated File */
package models.user

import java.time.LocalDateTime

import io.circe.{Decoder, Encoder}
import models.result.BaseResult
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.paging.PagingOptions
import io.circe.generic.semiauto._
import io.circe.java8.time._

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
  implicit val jsonEncoder: Encoder[UserResult] = deriveEncoder
  implicit val jsonDecoder: Decoder[UserResult] = deriveDecoder

  def fromRecords(
    q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int],
    startMs: Long, totalCount: Int, results: Seq[User]
  ) = {
    val paging = PagingOptions.from(totalCount, limit, offset)
    val durationMs = (System.currentTimeMillis - startMs).toInt
    UserResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = totalCount, results = results, durationMs = durationMs)
  }
}
