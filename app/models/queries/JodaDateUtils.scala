package models.queries

import java.time._

import util.DateUtils

trait JodaDateUtils {
  protected def toJoda(ldt: LocalDateTime): org.joda.time.LocalDateTime = new org.joda.time.LocalDateTime(DateUtils.toMillis(ldt))
  protected def toJoda(ld: LocalDate): org.joda.time.LocalDate = new org.joda.time.LocalDate(ld.toString)
  protected def toJoda(lt: LocalTime): org.joda.time.LocalTime = new org.joda.time.LocalTime(lt.toString)
  protected def fromJoda(ldt: org.joda.time.LocalDateTime): LocalDateTime = DateUtils.fromMillis(ldt.toDateTime(org.joda.time.DateTimeZone.UTC).getMillis)
  protected def fromJoda(ld: org.joda.time.LocalDate): LocalDate = LocalDate.parse(ld.toString)
  protected def fromJoda(lt: org.joda.time.LocalTime): LocalTime = LocalTime.parse(lt.toString)
}
