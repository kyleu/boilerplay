package utils

import org.joda.time._

object DateUtils {
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)
  implicit def localDateTimeOrdering: Ordering[LocalDateTime] = Ordering.fromLessThan(_ isBefore _)

  def today = DateTime.now(DateTimeZone.UTC).toLocalDate

  def now = DateTime.now(DateTimeZone.UTC).toLocalDateTime
  def nowMillis = toMillis(now)

  def toMillis(ldt: LocalDateTime) = ldt.toDateTime(DateTimeZone.UTC).getMillis
  def fromMillis(millis: Long) = new LocalDateTime(millis, DateTimeZone.UTC)
}
