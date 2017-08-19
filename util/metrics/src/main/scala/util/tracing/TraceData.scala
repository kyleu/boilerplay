package util.tracing

import brave.Span

case class TraceData(span: Span) {
  def log(s: String): Unit = span.annotate(s)
  def logViewClass(cls: Class[_]): Unit = log(cls.getSimpleName.stripSuffix("$") + ".scala.html")
}
