package services.graphql

import io.circe.Json
import io.circe.parser._
import models.Application
import models.graphql.{GraphQLContext, Schema, TracingExtension}
import models.user.User
import sangria.execution.{ExceptionHandler, Executor, HandledException, QueryReducer}
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.validation.QueryValidator
import services.ServiceRegistry
import util.FutureUtils.graphQlContext
import util.tracing.{TraceData, TracingService}
import util.Logging

import scala.util.{Failure, Success}

@javax.inject.Singleton
class GraphQLService @javax.inject.Inject() (tracing: TracingService, registry: ServiceRegistry) extends Logging {
  protected val exceptionHandler = ExceptionHandler {
    case (_, e: IllegalStateException) =>
      log.warn("Error encountered while running GraphQL query.", e)
      HandledException(message = e.getMessage, additionalFields = Map.empty)
  }

  private[this] val rejectComplexQueries = QueryReducer.rejectComplexQueries[Any](1000, (_, _) => new IllegalArgumentException(s"Query is too complex."))

  def executeQuery(app: Application, query: String, variables: Option[Json], operation: Option[String], user: User, debug: Boolean)(implicit t: TraceData) = {
    tracing.trace(s"graphql.service.execute.${operation.getOrElse("adhoc")}") { td =>
      if (!td.span.isNoop) {
        td.span.tag("query", query)
        variables.foreach(v => td.span.tag("variables", v.spaces2))
        operation.foreach(o => td.span.tag("operation", o))
        td.span.tag("debug", debug.toString)
      }

      QueryParser.parse(query) match {
        case Success(ast) =>
          td.span.annotate("parse.success")
          val ret = Executor.execute(
            schema = Schema.schema,
            queryAst = ast,
            userContext = GraphQLContext(app, registry, user, td),
            operationName = operation,
            variables = variables.getOrElse(Json.obj()),
            deferredResolver = Schema.resolver,
            exceptionHandler = exceptionHandler,
            maxQueryDepth = Some(10),
            queryValidator = QueryValidator.default,
            queryReducers = List(rejectComplexQueries),
            middleware = if (debug) { TracingExtension :: Nil } else { Nil }
          )
          ret
        case Failure(error) =>
          td.span.annotate(s"parse.failure")
          throw error
      }
    }
  }

  def parseVariables(variables: String) = if (variables.trim == "" || variables.trim == "null") {
    Json.obj()
  } else {
    parse(variables) match {
      case Right(x) => x
      case Left(_) => Json.obj()
    }
  }
}
