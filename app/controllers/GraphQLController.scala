package controllers

import io.circe.Json
import models.Application
import models.graphql.Schema
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.SyntaxError
import sangria.renderer.SchemaRenderer
import services.graphql.GraphQLService
import util.tracing.TraceData
import util.web.ControllerUtils.{jsonBody, jsonObject}

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLController @javax.inject.Inject() (override val app: Application, graphQLService: GraphQLService) extends BaseController("graphql") {
  import app.contexts.webContext
  private[this] val secretKey = "GraphTastesBad"

  def graphql(query: Option[String], variables: Option[String]) = act("graphql.ui") { implicit request => implicit td =>
    Future.successful(Ok(views.html.graphql.graphiql()))
  }

  def graphqlBody = act("graphql.post") { implicit request => implicit td =>
    val body = jsonObject(jsonBody(request.body)).filter(x => x._1 != "variables").toMap
    val query = body("query").as[String].getOrElse("{}")
    val variables = body.get("variables").map(x => graphQLService.parseVariables(x.toString))
    val operation = body.get("operationName").flatMap(_.asString)

    executeQuery(query, variables, operation, app.config.debug)
  }

  lazy val idl = SchemaRenderer.renderSchema(Schema.schema)

  def renderSchema() = act("graphql.schema") { implicit request => implicit td =>
    Future.successful(Ok(idl))
  }

  def voyager() = act("schema.render") { implicit request => implicit td =>
    Future.successful(Ok(views.html.graphql.voyager()))
  }

  private[this] def executeQuery(
    query: String, variables: Option[Json], operation: Option[String], debug: Boolean
  )(implicit data: TraceData) = {
    try {
      val f = graphQLService.executeQuery(app, query, variables, operation, debug)
      f.map(x => Ok(x)).recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver => InternalServerError(error.resolveError)
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
        Future.successful(BadRequest(json))
    }
  }
}
