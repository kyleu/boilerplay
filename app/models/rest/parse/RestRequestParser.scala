package models.rest.parse

import models.rest.{RequestBody, RestHeader, RestRequest}
import models.rest.enums.{ContentType, Method, MimeType}

object RestRequestParser {
  def fromFields(name: String, title: String, method: Method, path: String, version: String, headers: Seq[RestHeader], body: Option[String]) = {
    var contentType: ContentType = ContentType.Json
    var accept: MimeType = MimeType.Html

    val filteredHeaders = headers.flatMap {
      case h if h.k == RestHeader.contentType =>
        contentType = MimeType.values.find(_.mt == h.v).flatMap(x => ContentType.values.find(_.t == x)).getOrElse(contentType)
        None
      case h if h.k == RestHeader.accept =>
        accept = MimeType.values.find(_.mt == h.v).getOrElse(MimeType.Custom(h.v))
        None
      case x => Some(x)
    }

    val bodyContent = body.map(content => contentType match {
      case ContentType.Json => RequestBody.JsonContent(content)
      case _ => RequestBody.TextContent(content)
    })

    RestRequest(
      name = name,
      title = title,
      path = path,
      method = method,
      contentType = contentType,
      accept = accept,
      headers = filteredHeaders,
      body = bodyContent
    )
  }
}
