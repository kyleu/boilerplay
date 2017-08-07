package controllers.admin.graphql

import controllers.BaseController
import io.circe.Json
import models.user.RichUser
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.marshalling.MarshallingUtil._
import sangria.parser.SyntaxError
import services.graphql.GraphQLService
import util.{Application, FutureUtils}

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def graphql(query: Option[String], variables: Option[String]) = withAdminSession("graphql.ui") { implicit request =>
    Future.successful(Ok(views.html.admin.graphql.graphiql(request.identity)))
  }

  def graphqlBody = withAdminSession("graphql.post") { implicit request =>
    val json = {
      import sangria.marshalling.playJson._
      val playJson = request.body.asJson.getOrElse(play.api.libs.json.JsObject.empty)
      playJson.convertMarshaled[Json]
    }

    val body = json.asObject.getOrElse(throw new IllegalStateException(s"Invalid json [$json].")).filter(x => x._1 != "variables").toMap
    val query = body("query").as[String].getOrElse("{}")
    val variables = body.get("variables").map(x => GraphQLService.parseVariables(x.asString.getOrElse("{}")))
    val operation = body.get("operationName").flatMap(_.asString)

    executeQuery(query, variables, operation, models.user.RichUser(request.identity))
  }

  def executeQuery(query: String, variables: Option[Json], operation: Option[String], user: RichUser) = {
    try {
      val f = GraphQLService.executeQuery(app, query, variables, operation, user)
      f.map(x => Ok(x.spaces2).as("application/json"))(FutureUtils.defaultContext).recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError.spaces2).as("application/json")
        case error: ErrorWithResolver => InternalServerError(error.resolveError.spaces2).as("application/json")
      }(FutureUtils.defaultContext)
    } catch {
      case error: SyntaxError =>
        val json = Json.obj(
          "syntaxError" -> Json.fromString(error.getMessage),
          "locations" -> Json.arr(Json.obj(
            "line" -> Json.fromInt(error.originalError.position.line),
            "column" -> Json.fromInt(error.originalError.position.column)
          ))
        )
        Future.successful(BadRequest(json.spaces2).as("application/json"))
    }
  }
}
