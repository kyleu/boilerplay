package models.graphql

import java.time.LocalDateTime

import models.result.filter.{Filter, FilterSchema}
import models.result.orderBy.{OrderBy, OrderBySchema}
import models.result.paging.PagingOptions
import sangria.schema.{Args, Context}
import services.ModelServiceHelper
import util.tracing.TraceData
import zipkin.Endpoint

import scala.concurrent.Future
import util.FutureUtils.graphQlContext

abstract class SchemaHelper(val name: String) {
  private[this] lazy val endpoint = Endpoint.builder().serviceName(name + ".schema").build()

  protected def trace[A](ctx: GraphQLContext, k: String)(f: TraceData => Future[A]) = {
    implicit val traceData = ctx.trace
    ctx.app.tracing.trace(k) { tn =>
      tn.span.remoteEndpoint(endpoint)
      f(tn)
    }
  }

  protected case class SearchArgs(start: LocalDateTime, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])
  protected case class SearchResult[T](count: Int, results: Seq[T], args: SearchArgs) {
    val paging = PagingOptions.from(count, args.limit, args.offset)
    val dur = (System.currentTimeMillis - util.DateUtils.toMillis(args.start)).toInt
  }

  def argsFor(args: Args) = SearchArgs(
    start = util.DateUtils.now,
    filters = args.arg(FilterSchema.reportFiltersArg).getOrElse(Nil),
    orderBys = args.arg(OrderBySchema.orderBysArg).getOrElse(Nil),
    limit = args.arg(CommonSchema.limitArg),
    offset = args.arg(CommonSchema.offsetArg)
  )

  def runSearch[T](svc: ModelServiceHelper[T], c: Context[GraphQLContext, Unit])(implicit trace: TraceData) = {
    val args = argsFor(c.args)
    val f = c.arg(CommonSchema.queryArg) match {
      case Some(q) => svc.searchWithCount(q, args.filters, args.orderBys, args.limit, args.offset)(c.ctx.trace)
      case _ => svc.getAllWithCount(args.filters, args.orderBys, args.limit, args.offset)(c.ctx.trace)
    }
    f.map { x =>
      trace.span.annotate("Composing result.")
      SearchResult(x._1, x._2, args)
    }
  }
}
