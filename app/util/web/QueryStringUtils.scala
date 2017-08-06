package util.web

import java.time.{LocalDate, LocalDateTime}

import play.api.mvc.{PathBindable, QueryStringBindable}
import util.DateUtils

object QueryStringUtils {
  implicit def localDateTimePathBindable(implicit stringBinder: PathBindable[String]) = new PathBindable[LocalDateTime] {
    override def bind(key: String, value: String): Either[String, LocalDateTime] = stringBinder.bind(key, value) match {
      case Right(s) => Right(DateUtils.fromIsoString(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, ldt: LocalDateTime): String = DateUtils.toIsoString(ldt)
  }

  implicit def localDatePathBindable(implicit stringBinder: PathBindable[String]) = new PathBindable[LocalDate] {
    override def bind(key: String, value: String): Either[String, LocalDate] = stringBinder.bind(key, value) match {
      case Right(s) => Right(LocalDate.parse(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, ld: LocalDate): String = ld.toString
  }

  implicit def localDateTimeQueryStringBindable(implicit stringBinder: QueryStringBindable[String]) = new QueryStringBindable[LocalDateTime] {
    override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map {
      case Right(s) => Right(DateUtils.fromIsoString(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, ldt: LocalDateTime): String = DateUtils.toIsoString(ldt)
  }

  implicit def localDateQueryStringBindable(implicit stringBinder: QueryStringBindable[String]) = new QueryStringBindable[LocalDate] {
    override def bind(key: String, params: Map[String, Seq[String]]) = stringBinder.bind(key, params).map {
      case Right(s) => Right(LocalDate.parse(s))
      case Left(x) => throw new IllegalStateException(x)
    }
    override def unbind(key: String, ld: LocalDate): String = ld.toString
  }
}
