package utils

import org.joda.time.{ LocalDateTime, LocalTime, LocalDate }

object Formatter {
  private[this] val numFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US)

  def withCommas(i: Int) = numFormatter.format(i.toLong)
  def withCommas(l: Long) = numFormatter.format(l)
  def withCommas(d: Double) = numFormatter.format(d)
  def niceDate(d: LocalDate) = d.toString("EEEE, MMM dd, yyyy")
  def niceTime(d: LocalTime) = d.toString("HH:mm:ss")
  def niceDateTime(dt: LocalDateTime) = s"${niceDate(dt.toLocalDate)} ${niceTime(dt.toLocalTime)} UTC"
}
