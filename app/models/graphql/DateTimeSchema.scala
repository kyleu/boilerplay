package models.graphql

import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{LocalDate, LocalDateTime, LocalTime}
import sangria.marshalling.DateSupport
import sangria.schema._
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object DateTimeSchema {
  case object LocalDateTimeCoercionViolation extends ValueCoercionViolation("Date/time value expected in format [yyyy-MM-dd HH:mm:ss].")

  private[this] def parseLocalDateTime(s: String) = Try(new LocalDateTime(s)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateTimeCoercionViolation)
  }

  implicit val localDateTimeType = ScalarType[LocalDateTime](
    name = "DateTime",
    description = Some("A string representing a date, in format [yyyy-MM-dd HH:mm:ss]."),
    coerceOutput = (d, caps) => if (caps.contains(DateSupport)) {
      d.toDate
    } else {
      ISODateTimeFormat.dateTime().print(d)
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

  case object LocalDateCoercionViolation extends ValueCoercionViolation("Date value expected in format [yyyy-MM-dd].")

  private[this] def parseLocalDate(s: String) = Try(new LocalDate(s)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateCoercionViolation)
  }

  implicit val localDateType = ScalarType[LocalDate](
    name = "Date",
    description = Some("A string representing a date, in format [yyyy-MM-dd]."),
    coerceOutput = (d, caps) => if (caps.contains(DateSupport)) { d.toDate } else { d.toString("yyyy-MM-dd") },
    coerceUserInput = {
      case s: String => parseLocalDate(s)
      case _ => Left(LocalDateCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => parseLocalDate(s)
      case _ => Left(LocalDateCoercionViolation)
    }
  )

  case object LocalTimeCoercionViolation extends ValueCoercionViolation("Time value expected in format [HH:mm:ss].")

  private[this] def parseLocalTime(s: String) = Try(new LocalTime(s)) match {
    case Success(date) => Right(date)
    case Failure(_) => Left(LocalDateCoercionViolation)
  }

  implicit val localTimeType = ScalarType[LocalTime](
    name = "Time",
    description = Some("A string representing the time, in format [HH:mm:ss]."),
    coerceOutput = (t, _) => t.toString("HH:mm:ss"),
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
