package util

import java.time._

object JodaDateUtils {
  def toJoda(ldt: LocalDateTime) = new org.joda.time.LocalDateTime(DateUtils.toMillis(ldt))
  def fromJoda(ldt: org.joda.time.LocalDateTime) = DateUtils.fromMillis(ldt.toDateTime(org.joda.time.DateTimeZone.UTC).getMillis)
}
