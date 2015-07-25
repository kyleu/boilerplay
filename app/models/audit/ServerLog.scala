package models.audit

import org.joda.time.LocalDateTime
import play.twirl.api.Html

object ServerLog {
  sealed trait LogLevel {
    def toHtml: Html
  }

  object LogLevel {
    case object Trace extends LogLevel {
      override def toHtml = Html("""<span class="label label-success">Trace</span>""")
    }
    case object Debug extends LogLevel {
      override def toHtml = Html("""<span class="label label-primary">Debug</span>""")
    }
    case object Info extends LogLevel {
      override def toHtml = Html("""<span class="label label-info">Info</span>""")
    }
    case object Warn extends LogLevel {
      override def toHtml = Html("""<span class="label label-warning">Warn</span>""")
    }
    case object Error extends LogLevel {
      override def toHtml = Html("""<span class="label label-danger">Error</span>""")
    }
    case object Fatal extends LogLevel {
      override def toHtml = Html("""<span class="label label-danger">Fatal</span>""")
    }

    val all = Seq(Trace, Debug, Info, Warn, Error, Fatal)

    def fromString(s: String) = all.find(_.toString.toLowerCase == s.toLowerCase).getOrElse(throw new IllegalStateException())
  }
}

case class ServerLog(
  level: ServerLog.LogLevel,
  line: Int,
  logger: String,
  thread: String,
  message: String,
  occurred: LocalDateTime
)
