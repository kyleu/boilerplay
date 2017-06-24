package services.graphql

import models.graphql.{GraphQLContext, Schema}
import models.user.User
import utils.FutureUtils.defaultContext
import play.api.libs.json.{JsObject, Json}
import sangria.execution.{Executor, HandledException}
import sangria.marshalling.playJson._
import sangria.parser.QueryParser
import utils.Application

import scala.util.{Failure, Success}

object GraphQLService {
  private[this] val exceptionHandler: Executor.ExceptionHandler = {
    case (m, e: IllegalStateException) => HandledException(e.getMessage)
  }

  def executeQuery(app: Application, query: String, variables: Option[JsObject], operation: Option[String], user: User) = {
    val ctx = GraphQLContext(app, user)
    QueryParser.parse(query) match {
      case Success(ast) => Executor.execute(
        schema = Schema.schema,
        queryAst = ast,
        userContext = ctx,
        operationName = operation,
        variables = variables.getOrElse(Json.obj()),
        //deferredResolver = new GraphQLResolver(ctx),
        exceptionHandler = exceptionHandler,
        maxQueryDepth = Some(10)
      )
      case Failure(error) => throw error
    }
  }

  def parseVariables(variables: String) = if (variables.trim == "" || variables.trim == "null") {
    Json.obj()
  } else {
    Json.parse(variables).as[JsObject]
  }
}
