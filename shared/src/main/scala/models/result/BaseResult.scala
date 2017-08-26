package models.result

import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.paging.PagingOptions
import java.time.LocalDateTime

abstract class BaseResult[T] {
  def paging: PagingOptions
  def filters: Seq[Filter]
  def orderBys: Seq[OrderBy]
  def totalCount: Int
  def results: Seq[T]
  def durationMs: Int
  def occurred: LocalDateTime
}
