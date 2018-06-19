package controllers.admin.graphql

import controllers.BaseController
import graphql.GraphQLService
import io.circe.Json
import models.Application
import models.auth.Credentials
import models.user.Role
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.SyntaxError
import util.EncryptionUtils
import util.tracing.TraceData
import util.web.ControllerUtils.{jsonBody, jsonObject}

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLController @javax.inject.Inject() (override val app: Application, graphQLService: GraphQLService) extends BaseController("graphql") {
  import app.contexts.webContext
  private[this] val secretKey = "GraphTastesBad"

  def graphql(query: Option[String], variables: Option[String]) = withSession("graphql.ui", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.graphql.graphiql(request.identity)))
  }

  def graphqlBody = withoutSession("graphql.post") { implicit request => implicit td =>
    val allowed = request.identity match {
      case Some(u) if u.role == Role.Admin => true // All cool, admin
      case Some(_) => false
      case None if request.headers.get("admin-graphql-auth").exists(x => EncryptionUtils.decrypt(x) == secretKey) => true // All Cool, config backdoor
      case None =>
        val enc = EncryptionUtils.encrypt(secretKey)
        log.warn(s"Invalid graphql authentication. To access the server without logging in, add the header [admin-graphql-auth] with value [$enc].")
        false
    }

    if (allowed) {
      val body = jsonObject(jsonBody(request.body)).toMap
      val query = body("query").as[String].getOrElse("{}")
      val variables = body.get("variables").map(x => graphQLService.parseVariables(x.toString))
      val operation = body.get("operationName").flatMap(_.asString)

      executeQuery(query, variables, operation, Credentials.fromInsecureRequest(request), app.config.debug)
    } else {
      failRequest(request)
    }
  }

  private[this] def executeQuery(
    query: String, variables: Option[Json], operation: Option[String], creds: Credentials, debug: Boolean
  )(implicit data: TraceData) = {
    try {
      val f = graphQLService.executeQuery(app, query, variables, operation, creds, debug)
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
