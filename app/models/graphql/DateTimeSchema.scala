package models.graphql

import java.time._
import sangria.marshalling.DateSupport
import sangria.schema._
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object DateTimeSchema {
  case object LocalDateTimeCoercionViolation extends ValueCoercionViolation("Date/time value expected in format [yyyy-MM-dd HH:mm:ss].")
  case object LocalDateCoercionViolation extends ValueCoercionViolation("Date value expected in format [yyyy-MM-dd].")
  case object LocalTimeCoercionViolation extends ValueCoercionViolation("Time value expected in format [HH:mm:ss].")

  private[this] val fmtLocalDateTime = format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private[this] val fmtLocalDate = format.DateTimeFormatter.ISO_LOCAL_DATE
  private[this] val fmtLocalTime = format.DateTimeFormatter.ISO_LOCAL_TIME

  private[this] def parseLocalDateTime(s: String) = Try(LocalDateTime.parse(s, fmtLocalDateTime)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateTimeCoercionViolation)
  }

  implicit val localDateTimeType = ScalarType[LocalDateTime](
    name = "DateTime",
    description = Some("A string representing a date, in format [yyyy-MM-dd HH:mm:ss]."),
    coerceOutput = (dt, caps) => if (caps.contains(DateSupport)) {
      java.util.Date.from(dt.atZone(ZoneId.systemDefault()).toInstant)
    } else {
      fmtLocalDateTime.format(dt)
    },
    coerceUserInput = {
      case s: String => parseLocalDateTime(s)
      case _ => Left(LocalDateTimeCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => parseLocalDateTime(s)
      case _ => Left(LocalDateTimeCoercionViolation)
    }
  )

  private[this] def parseLocalDate(s: String) = Try(LocalDate.parse(s, fmtLocalDate)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateCoercionViolation)
  }

  implicit val localDateType = ScalarType[LocalDate](
    name = "Date",
    description = Some("A string representing a date, in format [yyyy-MM-dd]."),
    coerceOutput = (d, caps) => if (caps.contains(DateSupport)) {
      java.util.Date.from(d.atStartOfDay.atZone(ZoneId.systemDefault()).toInstant)
    } else {
      fmtLocalDate.format(d)
    },
    coerceUserInput = {
      case s: String => parseLocalDate(s)
      case _ => Left(LocalDateCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => parseLocalDate(s)
      case _ => Left(LocalDateCoercionViolation)
    }
  )

  private[this] def parseLocalTime(s: String) = Try(LocalTime.parse(s, fmtLocalTime)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateCoercionViolation)
  }

  implicit val localTimeType = ScalarType[LocalTime](
    name = "Time",
    description = Some("A string representing the time, in format [HH:mm:ss]."),
    coerceOutput = (t, _) => fmtLocalTime.format(t),
    coerceUserInput = {
      case s: String => parseLocalTime(s)
      case _ => Left(LocalDateCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => parseLocalTime(s)
      case _ => Left(LocalDateCoercionViolation)
    }
  )
}
