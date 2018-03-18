package models.rest.parse

import models.rest.{RestHeader, RestRequest}
import models.rest.enums.Method

import scala.io.Source

object HttpParser {
  def parse(name: String, title: String, content: String) = {
    var requestLine = ""
    var headerLines = Seq.empty[(String, String)]
    var bodyLines = Seq.empty[String]
    var status = "request"

    Source.fromString(content).getLines.foreach {
      case l if status == "request" =>
        requestLine = l
        status = "header"
      case l if status == "header" && l.isEmpty => status = "body"
      case l if status == "header" && l.indexOf(':') == -1 => throw new IllegalStateException(s"Invalid header line [$l].")
      case l if status == "header" => headerLines = headerLines :+ (l.substring(0, l.indexOf(':')) -> l.substring(l.indexOf(':') + 1).trim)
      case l if status == "body" => bodyLines = bodyLines :+ l
      case l => throw new IllegalStateException(s"Unhandled line [$l].")
    }

    val (method, path, version) = parseRequestLine(requestLine)
    val headers = headerLines.map(h => RestHeader(h._1.trim, h._2.trim))
    val body = bodyLines.mkString("\n").trim()
    RestRequestParser.fromFields(name, title, method, path, version, headers, if (body.isEmpty) { None } else { Some(body) })
  }

  private[this] def parseRequestLine(requestLine: String) = requestLine.split(' ').toList match {
    case method :: path :: version :: Nil => (Method.withValue(method.trim.toLowerCase), path.trim, version.trim)
    case _ => throw new IllegalStateException(s"Invalid request line [$requestLine].")
  }
}
