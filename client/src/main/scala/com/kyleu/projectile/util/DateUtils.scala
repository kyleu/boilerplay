package com.kyleu.projectile.util

import java.text.SimpleDateFormat
import java.time._

object DateUtils {
  private[this] val isoFmt = format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private[this] val isoZonedFmt = format.DateTimeFormatter.ISO_ZONED_DATE_TIME
  private[this] val dateFmt = format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private[this] val niceDateFmt = format.DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy")
  private[this] val timeFmt = format.DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)
  implicit def localDateTimeOrdering: Ordering[LocalDateTime] = Ordering.fromLessThan(_ isBefore _)

  def today = LocalDate.now()
  def now = LocalDateTime.now()
  def nowZoned = ZonedDateTime.now()
  def nowMillis = System.currentTimeMillis
  def currentTime = LocalTime.now()

  def toMillis(ldt: LocalDateTime) = ldt.atZone(ZoneId.systemDefault).toInstant.toEpochMilli
  def fromMillis(millis: Long) = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault).toLocalDateTime

  def toIsoString(ldt: LocalDateTime) = isoFmt.format(ldt)
  def fromIsoString(s: String) = LocalDateTime.from(isoFmt.parse(s))

  def toIsoStringZoned(zdt: ZonedDateTime) = isoZonedFmt.format(zdt)
  def fromIsoStringZoned(s: String) = ZonedDateTime.from(isoZonedFmt.parse(s))

  def fromDateString(s: String) = LocalDate.from(dateFmt.parse(s))
  def fromTimeString(s: String) = LocalTime.from(timeFmt.parse(s))

  def niceDate(d: LocalDate) = niceDateFmt.format(d)
  def niceTime(t: LocalTime) = timeFmt.format(t)
  def niceDateTime(dt: LocalDateTime) = s"${niceDate(dt.toLocalDate)} ${niceTime(dt.toLocalTime)} UTC"
  def niceDateTimeZoned(dt: ZonedDateTime) = s"${niceDate(dt.toLocalDate)} ${niceTime(dt.toLocalTime)} ${dt.getZone.getId}"

  private[this] val dFmt = new SimpleDateFormat("yyyy-MM-dd")
  def sqlDateFromString(s: String) = new java.sql.Date(dFmt.parse(s).getTime)

  private[this] val tFmt = new SimpleDateFormat("hh:mm:ss")
  def sqlTimeFromString(s: String) = new java.sql.Time(tFmt.parse(s).getTime)

  private[this] val dtFmtIso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  private[this] val dtFmtDefault = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  private[this] val dtFmtAmPm = new SimpleDateFormat("yyyy-MM-dd hh:mma")
  def sqlDateTimeFromString(s: String) = {
    def parse(sdf: SimpleDateFormat) = try {
      Some(new java.sql.Timestamp(sdf.parse(s).getTime))
    } catch {
      case _: java.text.ParseException => None
    }
    parse(dtFmtIso).orElse(parse(dtFmtDefault)).orElse(parse(dtFmtAmPm)).getOrElse(throw new IllegalStateException(s"Cannot parse timestamp from [$s]."))
  }
}
