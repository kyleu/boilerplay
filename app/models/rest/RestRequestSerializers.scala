package models.rest

object RestRequestSerializers {
  def toJson(req: RestRequest) = io.circe.syntax.EncoderOps(req).asJson

  def toRaw(req: RestRequest) = {
    val sb = new StringBuilder()
    def add(s: String = "") = sb.append(s + "\n")
    add(s"${req.method.toString.toUpperCase} ${req.path} HTTP/1.1")
    req.finalHeaders.foreach(h => add(h.toString))
    req.body.foreach {
      case t: RequestBody.TextContent => add("\n" + t.text)
      case j: RequestBody.JsonContent => add("\n" + j.jsonString)
      case b: RequestBody.BinaryContent => throw new IllegalStateException("TODO")
    }
    add()
    sb.toString
  }

  def toCurl(req: RestRequest) = {
    val sb = new StringBuilder()
    def add(s: String = "") = sb.append(s + "\n")
    add(s"")
    req.finalHeaders.foreach(h => add("???"))
    req.body.foreach {
      case t: RequestBody.TextContent => ???
      case j: RequestBody.JsonContent => ???
      case b: RequestBody.BinaryContent => throw new IllegalStateException("TODO")
    }
    add()
    sb.toString
  }
}
