package controllers.admin

import controllers.BaseController
import models.result.orderBy.OrderBy
import services.ModelServiceHelper
import util.tracing.TraceData

abstract class ServiceController[T](val svc: ModelServiceHelper[T]) extends BaseController(svc.key) {
  def search(q: Option[String], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit request: Req, traceData: TraceData) = {
    q match {
      case Some(query) if query.nonEmpty => svc.search(request, query, Nil, orderBys, limit.orElse(Some(100)), offset)
      case _ => svc.getAll(request, Nil, orderBys, limit.orElse(Some(100)), offset)
    }
  }

  def searchWithCount(q: Option[String], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit request: Req, traceData: TraceData) = {
    q match {
      case Some(query) if query.nonEmpty => svc.searchWithCount(request, query, Nil, orderBys, limit.orElse(Some(100)), offset)
      case _ => svc.getAllWithCount(request, Nil, orderBys, limit.orElse(Some(100)), offset)
    }
  }
}
