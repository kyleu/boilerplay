/* Generated File */
package models.note

import com.kyleu.projectile.models.result.BaseResult
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.result.paging.PagingOptions
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

final case class NoteRowResult(
    override val filters: Seq[Filter] = Nil,
    override val orderBys: Seq[OrderBy] = Nil,
    override val totalCount: Int = 0,
    override val paging: PagingOptions = PagingOptions(),
    override val results: Seq[NoteRow] = Nil,
    override val durationMs: Int = 0,
    override val occurred: LocalDateTime = DateUtils.now
) extends BaseResult[NoteRow]

object NoteRowResult {
  implicit val jsonEncoder: Encoder[NoteRowResult] = deriveEncoder
  implicit val jsonDecoder: Decoder[NoteRowResult] = deriveDecoder

  def fromRecords(
    q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None,
    startMs: Long, totalCount: Int, results: Seq[NoteRow]
  ) = {
    val paging = PagingOptions.from(totalCount, limit, offset)
    val durationMs = (DateUtils.nowMillis - startMs).toInt
    NoteRowResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = totalCount, results = results, durationMs = durationMs)
  }
}
