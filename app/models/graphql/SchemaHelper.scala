package models.graphql

import java.time.LocalDateTime

import models.result.filter.{Filter, FilterSchema}
import models.result.orderBy.{OrderBy, OrderBySchema}
import models.result.paging.PagingOptions
import sangria.schema.{Args, Context}
import services.ModelServiceHelper
import util.tracing.TraceData

import scala.concurrent.Future
import util.FutureUtils.graphQlContext

abstract class SchemaHelper(val name: String) {
  protected def trace[A](ctx: GraphQLContext, k: String)(f: TraceData => Future[A]) = ctx.app.tracing.trace(name + ".schema." + k)(f)(ctx.trace)

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

  def runSearch[T](svc: ModelServiceHelper[T], c: Context[GraphQLContext, Unit], td: TraceData) = {
    val args = argsFor(c.args)
    val f = c.arg(CommonSchema.queryArg) match {
      case Some(q) => svc.searchWithCount(q, args.filters, args.orderBys, args.limit, args.offset)(td)
      case _ => svc.getAllWithCount(args.filters, args.orderBys, args.limit, args.offset)(td)
    }
    f.map { x =>
      c.ctx.trace.span.annotate("Composing search result.")
      SearchResult(x._1, x._2, args)
    }
  }
}
