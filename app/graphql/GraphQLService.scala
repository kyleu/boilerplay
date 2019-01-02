package graphql

import com.google.inject.Injector
import com.kyleu.projectile.graphql.GraphQLContext
import io.circe.Json

import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.auth.UserCredentials
import sangria.execution.{ExceptionHandler, Executor, HandledException, QueryReducer}
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.validation.QueryValidator
import com.kyleu.projectile.util.{JsonSerializers, Logging}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import services.note.ModelNoteService

import scala.util.{Failure, Success}

@javax.inject.Singleton
class GraphQLService @javax.inject.Inject() (tracing: TracingService, noteService: ModelNoteService, injector: Injector) extends Logging {
  protected val exceptionHandler = ExceptionHandler {
    case (_, e: IllegalStateException) =>
      log.warn("Error encountered while running GraphQL query.", e)(TraceData.noop)
      HandledException(message = e.getMessage, additionalFields = Map.empty)
  }

  private[this] val rejectComplexQueries = QueryReducer.rejectComplexQueries[Any](1000, (_, _) => new IllegalArgumentException("Query is too complex."))

  def executeQuery(
    query: String, variables: Option[Json], operation: Option[String], creds: UserCredentials, debug: Boolean
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
            userContext = GraphQLContext(creds, tracing, td, injector, (creds, t, pk) => implicit td => noteService.getFor(creds, t, pk)(td)),
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
    JsonSerializers.parseJson(variables) match {
      case Right(x) => x
      case Left(_) => Json.obj()
    }
  }
}
