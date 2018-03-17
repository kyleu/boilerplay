package controllers.admin.graphql

import controllers.BaseController
import io.circe.Json
import models.Application
import models.auth.Credentials
import models.user.Role
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.SyntaxError
import services.graphql.GraphQLService
import util.tracing.TraceData
import util.web.ControllerUtils.{jsonBody, jsonObject}

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLController @javax.inject.Inject() (override val app: Application, graphQLService: GraphQLService) extends BaseController("graphql") {
  import app.contexts.webContext

  def graphql(query: Option[String], variables: Option[String]) = withSession("graphql.ui", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.graphql.graphiql(request.identity)))
  }

  def graphqlBody = withoutSession("graphql.post") { implicit request => implicit td =>
    request.identity match {
      case Some(u) if u.role == Role.Admin => // All cool, you're an admin
      case Some(_) => failRequest(request)
      case None if request.headers.get("admin-graphql-auth").contains(app.config.secretKey) => // All Cool, you can read graphql.config.json
      case None => failRequest(request)
    }

    val body = jsonObject(jsonBody(request.body)).filter(x => x._1 != "variables").toMap
    val query = body("query").as[String].getOrElse("{}")
    val variables = body.get("variables").map(x => graphQLService.parseVariables(x.asString.getOrElse("{}")))
    val operation = body.get("operationName").flatMap(_.asString)

    executeQuery(query, variables, operation, Credentials.fromInsecureRequest(request), app.config.debug)
  }

  private[this] def executeQuery(
    query: String, variables: Option[Json], operation: Option[String], creds: Credentials, debug: Boolean
  )(implicit data: TraceData) = {
    try {
      val f = graphQLService.executeQuery(app, query, variables, operation, creds, debug)
      f.map(x => Ok(x.spaces2).as(JSON)).recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError.spaces2).as(JSON)
        case error: ErrorWithResolver => InternalServerError(error.resolveError.spaces2).as(JSON)
      }
    } catch {
      case error: SyntaxError =>
        val json = Json.obj(
          "syntaxError" -> Json.fromString(error.getMessage),
          "locations" -> Json.arr(Json.obj(
            "line" -> Json.fromInt(error.originalError.position.line),
            "column" -> Json.fromInt(error.originalError.position.column)
          ))
        )
        Future.successful(BadRequest(json.spaces2).as(JSON))
    }
  }
}
