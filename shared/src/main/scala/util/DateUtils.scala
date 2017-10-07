package util

import java.text.SimpleDateFormat
import java.time._

object DateUtils {
  private[this] val isoFmt = format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private[this] val dateFmt = format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private[this] val niceDateFmt = format.DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")
  private[this] val niceTimeFmt = format.DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)
  implicit def localDateTimeOrdering: Ordering[LocalDateTime] = Ordering.fromLessThan(_ isBefore _)

  def today = LocalDate.now()
  def now = LocalDateTime.now()
  def nowMillis = toMillis(now)
  def currentTime = LocalTime.now()

  def toMillis(ldt: LocalDateTime) = ldt.atZone(ZoneId.systemDefault).toInstant.toEpochMilli
  def fromMillis(millis: Long) = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault).toLocalDateTime

  def toIsoString(ldt: LocalDateTime) = isoFmt.format(ldt)
  def fromIsoString(s: String) = LocalDateTime.from(isoFmt.parse(s))
  def fromDateString(s: String) = LocalDate.from(dateFmt.parse(s))

  def niceDate(d: LocalDate) = niceDateFmt.format(d)
  def niceTime(t: LocalTime) = niceTimeFmt.format(t)
  def niceDateTime(dt: LocalDateTime) = s"${niceDate(dt.toLocalDate)} ${niceTime(dt.toLocalTime)} UTC"

  private[this] val dFmt = new SimpleDateFormat("yyyy-MM-dd")
  def sqlDateFromString(s: String) = new java.sql.Date(dFmt.parse(s).getTime)

  private[this] val tFmt = new SimpleDateFormat("hh:mm:ss")
  def sqlTimeFromString(s: String) = new java.sql.Time(tFmt.parse(s).getTime)

  private[this] val dtFmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
  def sqlDateTimeFromString(s: String) = new java.sql.Timestamp(dtFmt.parse(s).getTime)
}
