package util

import java.time._

object JodaDateUtils {
  def toJoda(ldt: LocalDateTime): org.joda.time.LocalDateTime = new org.joda.time.LocalDateTime(DateUtils.toMillis(ldt))
  def toJoda(ld: LocalDate): org.joda.time.LocalDate = new org.joda.time.LocalDate(ld.toString)
  def toJoda(lt: LocalTime): org.joda.time.LocalTime = new org.joda.time.LocalTime(lt.toString)
  def fromJoda(ldt: org.joda.time.LocalDateTime): LocalDateTime = DateUtils.fromMillis(ldt.toDateTime(org.joda.time.DateTimeZone.UTC).getMillis)
  def fromJoda(ld: org.joda.time.LocalDate): LocalDate = LocalDate.parse(ld.toString)
  def fromJoda(lt: org.joda.time.LocalTime): LocalTime = LocalTime.parse(lt.toString)
}
