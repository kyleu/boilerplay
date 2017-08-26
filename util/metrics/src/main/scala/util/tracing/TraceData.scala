package util.tracing

import brave.Span

case class TraceData(span: Span) {
  def logViewClass(cls: Class[_]): Unit = span.annotate(cls.getSimpleName.stripSuffix("$") + ".scala.html")
}
