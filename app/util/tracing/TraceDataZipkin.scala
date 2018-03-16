package util.tracing

import brave.Span

case class TraceDataZipkin(span: Span) extends TraceData {
  override val isNoop = span.isNoop

  override def tag(k: String, v: String) = span.tag(k, v)
  override def annotate(v: String) = span.annotate(v)

  override def logViewClass(cls: Class[_]): Unit = span.annotate(cls.getSimpleName.stripSuffix("$") + ".scala.html")

  override def toString = s"${span.context.traceId} / ${span.context.spanId} (${span.context.sampled})"
}
