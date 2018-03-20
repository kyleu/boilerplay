package services.rest.parse

import models.rest.http._
import models.rest.request.RestRequest

object RestRequestParser {
  def fromFields(name: String, title: String, method: Method, path: String, version: String, headers: Seq[RestHeader], body: Option[String]) = {
    var contentType: MimeType = MimeType.Json
    var accept: MimeType = MimeType.Html

    val filteredHeaders = headers.flatMap {
      case h if h.k == RestHeader.contentType =>
        contentType = MimeType.forString(h.v)
        None
      case h if h.k == RestHeader.accept =>
        accept = MimeType.forString(h.v)
        None
      case x => Some(x)
    }

    val bodyContent = body.map(content => contentType match {
      case MimeType.Json => RestBody.JsonContent(content)
      case _ => RestBody.TextContent(content)
    })

    RestRequest(
      name = name,
      title = title,
      folder = Some(path),
      method = method,
      contentType = contentType,
      accept = accept,
      headers = filteredHeaders,
      body = bodyContent
    )
  }
}
