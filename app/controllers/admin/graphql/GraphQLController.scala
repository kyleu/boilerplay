package controllers.admin.graphql

import controllers.BaseController
import io.circe.Json
import models.Application
import models.user.User
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.MarshallingUtil._
import sangria.marshalling.circe._
import sangria.parser.SyntaxError
import services.graphql.GraphQLService
import util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLController @javax.inject.Inject() (override val app: Application, graphQLService: GraphQLService) extends BaseController("graphql") {
  import app.contexts.webContext

  def graphql(query: Option[String], variables: Option[String]) = withSession("graphql.ui", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.graphql.graphiql(request.identity)))
  }

  def graphqlBody = withSession("graphql.post", admin = true) { implicit request => implicit td =>
    val json = {
      import sangria.marshalling.playJson._
      val playJson = request.body.asJson.getOrElse(play.api.libs.json.JsObject.empty)
      playJson.convertMarshaled[Json]
    }

    val body = json.asObject.getOrElse(throw new IllegalStateException(s"Invalid json [$json].")).filter(x => x._1 != "variables").toMap
    val query = body("query").as[String].getOrElse("{}")
    val variables = body.get("variables").map(x => graphQLService.parseVariables(x.asString.getOrElse("{}")))
    val operation = body.get("operationName").flatMap(_.asString)

    executeQuery(query, variables, operation, request.identity, app.config.debug)
  }

  def executeQuery(query: String, variables: Option[Json], operation: Option[String], user: User, debug: Boolean)(implicit data: TraceData) = {
    try {
      val f = graphQLService.executeQuery(app, query, variables, operation, user, debug)
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
