package controllers.admin

import controllers.BaseController
import models.result.orderBy.OrderBy
import play.api.http.MimeTypes
import play.api.mvc.{Accepting, RequestHeader, Result}
import services.ModelServiceHelper
import util.tracing.TraceData

object ServiceController {
  object MimeTypes {
    val csv = "text/csv"
    val png = "image/png"
    val svg = "image/svg+xml"
  }

  val acceptsCsv = Accepting(MimeTypes.csv)
  val acceptsPng = Accepting(MimeTypes.png)
  val acceptsSvg = Accepting(MimeTypes.svg)
}

abstract class ServiceController[T](val svc: ModelServiceHelper[T]) extends BaseController(svc.key) {
  protected def search(q: Option[String], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit request: Req, traceData: TraceData) = {
    q match {
      case Some(query) if query.nonEmpty => svc.search(request, q, Nil, orderBys, limit.orElse(Some(100)), offset)
      case _ => svc.getAll(request, Nil, orderBys, limit.orElse(Some(100)), offset)
    }
  }

  protected def searchWithCount(
    q: Option[String], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]
  )(implicit request: Req, traceData: TraceData) = {
    q match {
      case Some(query) if query.nonEmpty => svc.searchWithCount(request, q, Nil, orderBys, limit.orElse(Some(100)), offset)
      case _ => svc.getAllWithCount(request, Nil, orderBys, limit.orElse(Some(100)), offset)
    }
  }

  protected def renderChoice(t: Option[String])(f: String => Result)(implicit request: RequestHeader) = t match {
    case None => render {
      case Accepts.Html() => f(MimeTypes.HTML)
      case Accepts.Json() => f(MimeTypes.JSON)
      case ServiceController.acceptsCsv() => f(ServiceController.MimeTypes.csv)
      case ServiceController.acceptsPng() => f(ServiceController.MimeTypes.png)
      case ServiceController.acceptsSvg() => f(ServiceController.MimeTypes.svg)
    }
    case Some("csv") => f(ServiceController.MimeTypes.csv)
    case Some("html") => f(MimeTypes.HTML)
    case Some("json") => f(MimeTypes.JSON)
    case Some("png") => f(ServiceController.MimeTypes.png)
    case Some("svg") => f(ServiceController.MimeTypes.svg)
    case Some(x) => throw new IllegalStateException(s"Unhandled output format [$x].")
  }

  def csvResponse(filename: String, content: String) = {
    val fn = if (filename.endsWith(".csv")) { filename } else { filename + ".csv" }
    Ok(content).as(ServiceController.MimeTypes.csv).withHeaders("Content-Disposition" -> s"""inline; filename="$fn.csv"""")
  }
}
