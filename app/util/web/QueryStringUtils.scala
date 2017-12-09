package util.web

import java.time.{LocalDate, LocalDateTime, LocalTime}

import play.api.mvc.{PathBindable, QueryStringBindable}
import util.DateUtils

object QueryStringUtils {
  implicit def localDateTimePathBindable(implicit stringBinder: PathBindable[String]): PathBindable[LocalDateTime] = new PathBindable[LocalDateTime] {
    override def bind(key: String, value: String) = stringBinder.bind(key, value) match {
      case Right(s) => Right(DateUtils.fromIsoString(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, ldt: LocalDateTime) = DateUtils.toIsoString(ldt)
  }

  implicit def localDatePathBindable(implicit stringBinder: PathBindable[String]): PathBindable[LocalDate] = new PathBindable[LocalDate] {
    override def bind(key: String, value: String) = stringBinder.bind(key, value) match {
      case Right(s) => Right(LocalDate.parse(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, ld: LocalDate) = ld.toString
  }

  implicit def localTimePathBindable(implicit stringBinder: PathBindable[String]): PathBindable[LocalTime] = new PathBindable[LocalTime] {
    override def bind(key: String, value: String) = stringBinder.bind(key, value) match {
      case Right(s) => Right(LocalTime.parse(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, lt: LocalTime) = lt.toString
  }

  implicit def bytePathBindable(implicit intBinder: PathBindable[Int]): PathBindable[Byte] = new PathBindable[Byte] {
    override def bind(key: String, value: String) = intBinder.bind(key, value) match {
      case Right(x) => Right(x.toByte)
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, b: Byte) = b.toInt.toString
  }

  implicit def localDateTimeQueryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[LocalDateTime] = {
    new QueryStringBindable[LocalDateTime] {
      override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map {
        case Right(s) => Right(DateUtils.fromIsoString(s))
        case Left(x) => throw new IllegalStateException(x)
      }
      override def unbind(key: String, ldt: LocalDateTime): String = DateUtils.toIsoString(ldt)
    }
  }

  implicit def localDateQueryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[LocalDate] = {
    new QueryStringBindable[LocalDate] {
      override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map {
        case Right(s) => Right(LocalDate.parse(s))
        case Left(x) => throw new IllegalStateException(x)
      }
      override def unbind(key: String, ld: LocalDate): String = ld.toString
    }
  }

  implicit def localTimeQueryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[LocalTime] = {
    new QueryStringBindable[LocalTime] {
      override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map {
        case Right(s) => Right(LocalTime.parse(s))
        case Left(x) => throw new IllegalStateException(x)
      }
      override def unbind(key: String, lt: LocalTime): String = lt.toString
    }
  }

  implicit def byteQueryStringBindable(implicit intBinder: QueryStringBindable[Int]): QueryStringBindable[Byte] = {
    new QueryStringBindable[Byte] {
      override def bind(key: String, params: Map[String, Seq[String]]) = intBinder.bind(key, params).map {
        case Right(s) => Right(s.toByte)
        case Left(x) => throw new IllegalStateException(x)
      }
      override def unbind(key: String, lt: Byte): String = lt.toString
    }
  }
}
