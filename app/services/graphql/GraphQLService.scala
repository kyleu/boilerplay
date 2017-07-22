package services.graphql

import io.circe.Json
import io.circe.parser._
import models.graphql.{GraphQLContext, Schema}
import models.user.User
import sangria.execution.{Executor, HandledException}
import sangria.marshalling.circe._
import sangria.parser.QueryParser
import util.Application
import util.FutureUtils.defaultContext

import scala.util.{Failure, Success}

object GraphQLService {
  private[this] val exceptionHandler: Executor.ExceptionHandler = {
    case (_, e: IllegalStateException) => HandledException(e.getMessage)
  }

  def executeQuery(app: Application, query: String, variables: Option[Json], operation: Option[String], user: User) = {
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
        maxQueryDepth = Some(10)
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
