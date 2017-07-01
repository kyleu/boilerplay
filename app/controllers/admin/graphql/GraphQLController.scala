package controllers.admin.graphql

import java.util.UUID

import controllers.BaseController
import models.user.User
import play.api.libs.json._
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.playJson._
import sangria.parser.SyntaxError
import services.graphql.{GraphQLFileService, GraphQLService}
import utils.{Application, FutureUtils}

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def graphql(query: Option[String], dir: Option[String], name: Option[String], variables: Option[String]) = {
    withAdminSession("graphql.ui") { implicit request =>
      val vars = variables.getOrElse("{}")
      log.debug(s"Executing GraphQL query [${query.getOrElse("")}] (${dir.getOrElse("-")}:${name.getOrElse("-")}) with [$vars] as variables.")
      Future.successful(Ok(views.html.admin.graphql.graphiql(request.identity, GraphQLFileService.list())))
    }
  }

  def graphqlBody = withAdminSession("graphql.post") { implicit request =>
    val body = request.body.asJson.getOrElse(throw new IllegalStateException("Missing JSON body."))
    val query = (body \ "query").as[String]
    val operation = (body \ "operationName").asOpt[String]

    val variables = (body \ "variables").toOption.flatMap {
      case JsString(vars) => Some(GraphQLService.parseVariables(vars))
      case obj: JsObject => Some(obj)
      case _ => None
    }

    executeQuery(query, variables, operation, request.identity)
  }

  def executeQuery(query: String, variables: Option[JsObject], operation: Option[String], user: User) = {
    try {
      val f = GraphQLService.executeQuery(app, query, variables, operation, user)
      f.map(Ok(_))(FutureUtils.defaultContext).recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver => InternalServerError(error.resolveError)
      }(FutureUtils.defaultContext)
    } catch {
      case error: SyntaxError => Future.successful(BadRequest(Json.obj(
        "syntaxError" -> error.getMessage,
        "locations" -> Json.arr(Json.obj(
          "line" -> error.originalError.position.line,
          "column" -> error.originalError.position.column
        ))
      )))
    }
  }
}
