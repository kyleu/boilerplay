package graphql

import io.circe.Json
import models.Application
import models.auth.Credentials
import sangria.execution.{ExceptionHandler, Executor, HandledException, QueryReducer}
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.validation.QueryValidator
import services.ServiceRegistry
import util.FutureUtils.graphQlContext
import util.Logging
import util.tracing.{TraceData, TracingService}

import scala.util.{Failure, Success}

@javax.inject.Singleton
class GraphQLService @javax.inject.Inject() (tracing: TracingService, registry: ServiceRegistry) extends Logging {
  protected val exceptionHandler = ExceptionHandler {
    case (_, e: IllegalStateException) =>
      log.warn("Error encountered while running GraphQL query.", e)
      HandledException(message = e.getMessage, additionalFields = Map.empty)
  }

  private[this] val rejectComplexQueries = QueryReducer.rejectComplexQueries[Any](1000, (_, _) => new IllegalArgumentException("Query is too complex."))

  def executeQuery(
    app: Application, query: String, variables: Option[Json], operation: Option[String], creds: Credentials, debug: Boolean
  )(implicit t: TraceData) = {
    tracing.trace(s"graphql.service.execute.${operation.getOrElse("adhoc")}") { td =>
      if (!td.isNoop) {
        td.tag("query", query)
        variables.foreach(v => td.tag("variables", v.spaces2))
        operation.foreach(o => td.tag("operation", o))
        td.tag("debug", debug.toString)
      }

      QueryParser.parse(query) match {
        case Success(ast) =>
          td.annotate("parse.success")
          val ret = Executor.execute(
            schema = Schema.schema,
            queryAst = ast,
            userContext = GraphQLContext(app, this, registry, creds, td),
            operationName = operation,
            variables = variables.getOrElse(Json.obj()),
            deferredResolver = Schema.resolver,
            exceptionHandler = exceptionHandler,
            maxQueryDepth = Some(10),
            queryValidator = QueryValidator.default,
            queryReducers = List[QueryReducer[GraphQLContext, _]](rejectComplexQueries),
            middleware = if (debug) { TracingExtension :: Nil } else { Nil }
          )
          ret
        case Failure(error) =>
          td.annotate("parse.failure")
          throw error
      }
    }
  }

  def parseVariables(variables: String) = if (variables.trim == "" || variables.trim == "null") {
    Json.obj()
  } else {
    util.JsonSerializers.parseJson(variables) match {
      case Right(x) => x
      case Left(_) => Json.obj()
    }
  }
}
