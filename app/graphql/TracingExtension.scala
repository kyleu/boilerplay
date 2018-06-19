package graphql

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue

import sangria.ast._
import sangria.execution._
import sangria.marshalling.queryAst._
import sangria.renderer.SchemaRenderer
import sangria.schema.Context

import scala.collection.JavaConverters._

object TracingExtension extends Middleware[Any] with MiddlewareExtension[Any] with MiddlewareAfterField[Any] with MiddlewareErrorField[Any] {
  final case class QueryTrace(startTime: Instant, startNanos: Long, fieldData: ConcurrentLinkedQueue[Value])

  override type QueryVal = QueryTrace
  override type FieldVal = Long

  override def beforeQuery(context: MiddlewareQueryContext[Any, _, _]) =
    QueryTrace(Instant.now(), System.nanoTime(), new ConcurrentLinkedQueue)

  override def afterQuery(queryVal: QueryVal, context: MiddlewareQueryContext[Any, _, _]) = ()

  override def beforeField(queryVal: QueryVal, mctx: MiddlewareQueryContext[Any, _, _], ctx: Context[Any, _]) =
    continue(System.nanoTime())

  override def afterField(queryVal: QueryVal, fieldVal: FieldVal, value: Any, mctx: MiddlewareQueryContext[Any, _, _], ctx: Context[Any, _]) = {
    updateMetric(queryVal, fieldVal, ctx)
    None
  }

  override def fieldError(queryVal: QueryVal, fieldVal: FieldVal, error: Throwable, mctx: MiddlewareQueryContext[Any, _, _], ctx: Context[Any, _]) = {
    updateMetric(queryVal, fieldVal, ctx)
  }

  override def afterQueryExtensions(queryVal: QueryVal, context: MiddlewareQueryContext[Any, _, _]): Vector[Extension[_]] = Vector(Extension(
    ObjectValue(Vector(
      ObjectField("tracing", ObjectValue(Vector(
        ObjectField("version", IntValue(1)),
        ObjectField("startTime", StringValue(DateTimeFormatter.ISO_INSTANT.format(queryVal.startTime))),
        ObjectField("endTime", StringValue(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))),
        ObjectField("duration", BigIntValue(System.nanoTime() - queryVal.startNanos)),
        ObjectField("execution", ObjectValue(Vector(
          ObjectField("resolvers", ListValue(queryVal.fieldData.asScala.toVector))
        )))
      )))
    )): Value
  ))

  private[this] def updateMetric(queryVal: QueryVal, fieldVal: FieldVal, ctx: Context[Any, _]): Unit = queryVal.fieldData.add(
    ObjectValue(Vector(
      ObjectField("path", ListValue(ctx.path.path.map(queryAstResultMarshaller.scalarNode(_, "Any", Set.empty)))),
      ObjectField("fieldName", StringValue(ctx.field.name)),
      ObjectField("parentType", StringValue(ctx.parentType.name)),
      ObjectField("returnType", StringValue(SchemaRenderer.renderTypeName(ctx.field.fieldType))),
      ObjectField("startOffset", BigIntValue(fieldVal - queryVal.startNanos)),
      ObjectField("duration", BigIntValue(System.nanoTime() - fieldVal))
    ))
  )
}
