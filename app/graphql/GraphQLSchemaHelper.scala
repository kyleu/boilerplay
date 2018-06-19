package graphql

import java.time.LocalDateTime

import models.result.filter.{Filter, FilterSchema}
import models.result.orderBy.{OrderBy, OrderBySchema}
import models.result.paging.PagingOptions
import sangria.schema.{Args, Argument, Context, Field, OutputType, fields}
import services.ModelServiceHelper
import util.tracing.TraceData
import util.FutureUtils.graphQlContext

import scala.concurrent.Future

object GraphQLSchemaHelper {
  final case class SearchArgs(start: LocalDateTime, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])
  final case class SearchResult[T](count: Int, results: Seq[T], args: SearchArgs) {
    val paging = PagingOptions.from(count, args.limit, args.offset)
    val dur = (System.currentTimeMillis - util.DateUtils.toMillis(args.start)).toInt
  }
}

abstract class GraphQLSchemaHelper(val name: String) {
  protected def traceF[A](ctx: GraphQLContext, k: String)(f: TraceData => Future[A]) = ctx.app.tracing.trace(name + ".schema." + k)(f)(ctx.trace)
  protected def traceB[A](ctx: GraphQLContext, k: String)(f: TraceData => A) = ctx.app.tracing.traceBlocking(name + ".schema." + k)(f)(ctx.trace)

  protected def typedField[In, Out](
    name: String,
    desc: Option[String],
    t: OutputType[Out],
    f: (Context[GraphQLContext, In], TraceData) => Future[Out],
    args: Argument[_]*
  ): Field[GraphQLContext, In] = {
    Field[GraphQLContext, In, Out, Out](name = name, description = desc, fieldType = t, arguments = args.toList, resolve = c => {
      traceF(c.ctx, name)(td => f(c, td))
    })
  }
  protected def unitField[Out](
    name: String,
    desc: Option[String],
    t: OutputType[Out],
    f: (Context[GraphQLContext, Unit], TraceData) => Future[Out],
    args: Argument[_]*
  ): Field[GraphQLContext, Unit] = typedField(name, desc, t, f, args: _*)

  protected def argsFor(args: Args) = GraphQLSchemaHelper.SearchArgs(
    start = util.DateUtils.now,
    filters = args.arg(FilterSchema.reportFiltersArg).getOrElse(Nil),
    orderBys = args.arg(OrderBySchema.orderBysArg).getOrElse(Nil),
    limit = args.arg(CommonSchema.limitArg),
    offset = args.arg(CommonSchema.offsetArg)
  )

  protected def runSearch[T](svc: ModelServiceHelper[T], c: Context[GraphQLContext, Unit], td: TraceData) = {
    val args = argsFor(c.args)
    val f = c.arg(CommonSchema.queryArg) match {
      case Some(q) if q.nonEmpty => svc.searchWithCount(c.ctx.creds, Some(q), args.filters, args.orderBys, args.limit, args.offset)(td)
      case _ => svc.getAllWithCount(c.ctx.creds, args.filters, args.orderBys, args.limit, args.offset)(td)
    }
    c.ctx.trace.annotate("Composing search result.")
    f.map(x => GraphQLSchemaHelper.SearchResult(x._1, x._2, args))
  }
}
