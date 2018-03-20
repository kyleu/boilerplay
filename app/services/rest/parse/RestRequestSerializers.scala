package services.rest.parse

import models.rest.http.RestBody
import models.rest.request.RestRequest

object RestRequestSerializers {
  def toJson(req: RestRequest) = io.circe.syntax.EncoderOps(req).asJson

  def toRaw(req: RestRequest) = {
    val sb = new StringBuilder()
    def add(s: String = "") = sb.append(s + "\n")
    add(s"${req.method.toString.toUpperCase} ${req.url.location} HTTP/1.1")
    req.finalHeaders.foreach(h => add(h.toString))
    req.body.foreach {
      case t: RestBody.TextContent => add("\n" + t.text)
      case j: RestBody.JsonContent => add("\n" + j.jsonString)
      case _: RestBody.BinaryContent => throw new IllegalStateException("TODO")
    }
    add()
    sb.toString
  }

  def toCurl(req: RestRequest) = {
    val sb = new StringBuilder()
    def add(s: String = "") = sb.append(s + "\n")
    add(s"""curl -X "POST" "${req.url}" \\""")
    req.finalHeaders.foreach(h => add(s"     -H '${h.k}: ${h.v}' \\"))
    req.body.foreach {
      case t: RestBody.TextContent => add("     -d $'" + t.text + "'")
      case j: RestBody.JsonContent => add("     -d $'" + j.jsonString + "'")
      case _: RestBody.BinaryContent => throw new IllegalStateException("TODO")
    }
    add()
    sb.toString
  }
}
