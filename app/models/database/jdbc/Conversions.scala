package models.database.jdbc

import java.time.{LocalDate, LocalDateTime}
import util.DateUtils

object Conversions {
  @SuppressWarnings(Array("MethodReturningAny"))
  def convert(x: AnyRef): AnyRef = x match {
    case num: BigDecimal => num.underlying()
    case num: BigInt => BigDecimal(num).underlying()

    // Convert Joda times to UTC.
    case d: LocalDate => new java.sql.Date(DateUtils.toMillis(d.atStartOfDay))
    case d: LocalDateTime => new java.sql.Date(DateUtils.toMillis(d))

    // Pass everything else through.
    case _ => x
  }
}
