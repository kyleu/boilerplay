package util.tracing

class TraceData {
  def isNoop: Boolean = true

  def tag(k: String, v: String): Unit = {}
  def annotate(v: String): Unit = {}

  def logClass(cls: Class[_]): Unit = {}
}
