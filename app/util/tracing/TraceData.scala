package util.tracing

class TraceData {
  def isNoop: Boolean = true

  def tag(k: String, v: String): Unit = {}
  def annotate(v: String): Unit = {}

  def logViewClass(cls: Class[_]): Unit = {}
}
