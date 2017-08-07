package services.graphql

import io.circe.Json
import io.circe.parser._
import models.graphql.{GraphQLContext, Schema}
import models.user.RichUser
import sangria.execution.{Executor, HandledException, QueryReducer}
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import sangria.validation.QueryValidator
import util.Application
import util.FutureUtils.defaultContext

import scala.util.{Failure, Success}

object GraphQLService {
  private[this] val exceptionHandler: Executor.ExceptionHandler = {
    case (_, e: IllegalStateException) => HandledException(e.getMessage)
  }

  private[this] val rejectComplexQueries = QueryReducer.rejectComplexQueries[Any](1000, (_, _) => new IllegalArgumentException(s"Query is too complex."))

  def executeQuery(app: Application, query: String, variables: Option[Json], operation: Option[String], user: RichUser) = {
    val ctx = GraphQLContext(app, user)
    QueryParser.parse(query) match {
      case Success(ast) => Executor.execute(
        schema = Schema.schema,
        queryAst = ast,
        userContext = ctx,
        operationName = operation,
        variables = variables.getOrElse(Json.obj()),
        deferredResolver = Schema.resolver,
        exceptionHandler = exceptionHandler,
        maxQueryDepth = Some(10),
        queryValidator = QueryValidator.default,
        queryReducers = List(rejectComplexQueries)
      )
      case Failure(error) => throw error
    }
  }

  def parseVariables(variables: String) = if (variables.trim == "" || variables.trim == "null") {
    Json.obj()
  } else {
    parse(variables) match {
      case Right(x) => x
      case Left(x) => Json.obj()
    }
  }
}
