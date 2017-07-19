package util

import java.text.SimpleDateFormat

object DateUtils {
  import java.time._

  private[this] val isoFmt = format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private[this] val niceDateFmt = format.DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")
  private[this] val niceTimeFmt = format.DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)
  implicit def localDateTimeOrdering: Ordering[LocalDateTime] = Ordering.fromLessThan(_ isBefore _)

  def today = LocalDate.now()
  def now = LocalDateTime.now()
  def nowMillis = toMillis(now)

  def toMillis(ldt: LocalDateTime) = ldt.atZone(ZoneId.systemDefault).toInstant.toEpochMilli
  def fromMillis(millis: Long) = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault).toLocalDateTime

  def toIsoString(ldt: LocalDateTime) = isoFmt.format(ldt)

  def niceDate(d: LocalDate) = niceDateFmt.format(d)
  def niceTime(t: LocalTime) = niceTimeFmt.format(t)
  def niceDateTime(dt: LocalDateTime) = s"${niceDate(dt.toLocalDate)} ${niceTime(dt.toLocalTime)} UTC"

  def toJoda(ldt: LocalDateTime) = new org.joda.time.LocalDateTime(toMillis(ldt))
  def fromJoda(ldt: org.joda.time.LocalDateTime) = fromMillis(ldt.toDateTime(org.joda.time.DateTimeZone.UTC).getMillis)

  private[this] val dFmt = new SimpleDateFormat("yyyy-MM-dd")
  def sqlDateFromString(s: String) = new java.sql.Date(dFmt.parse(s).getTime)

  private[this] val tFmt = new SimpleDateFormat("hh:mm:ss")
  def sqlTimeFromString(s: String) = new java.sql.Time(tFmt.parse(s).getTime)

  private[this] val dtFmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
  def sqlDateTimeFromString(s: String) = new java.sql.Timestamp(dtFmt.parse(s).getTime)
}
