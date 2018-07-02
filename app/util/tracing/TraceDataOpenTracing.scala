package util.tracing

import io.opentracing.Span

final case class TraceDataOpenTracing(span: Span) extends TraceData {
  override val isNoop = false

  override def tag(k: String, v: String) = span.setTag(k, v)
  override def annotate(v: String) = span.log(v)

  override def logViewClass(cls: Class[_]): Unit = span.log(cls.getSimpleName.stripSuffix("$") + ".scala.html")

  override def toString = span.context.toString
}
