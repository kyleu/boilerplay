package controllers.admin.graphql

import controllers.BaseController
import io.circe.Json
import io.circe.syntax._
import models.Application
import models.request.GraphQLRequest
import services.file.FileRepository
import services.graphql.GraphQLFileService
import util.tracing.TraceData

import scala.concurrent.Future
import scala.util.control.NonFatal

@javax.inject.Singleton
class GraphQLRepositoryController @javax.inject.Inject() (override val app: Application, files: GraphQLFileService) extends BaseController("graphqlRepo") {
  import app.contexts.webContext
  private[this] val key = "graphql"

  def newQuery() = withSession("new", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.graphql.queryDetail(request.identity, "new", GraphQLRequest("New Query"))))
  }

  def listRoot = withSession("list", admin = true) { implicit request => implicit td =>
    listResponse(None)
  }

  def listPath(path: String) = withSession("list", admin = true) { implicit request => implicit td =>
    listResponse(Some(path))
  }

  def queryDetail(path: String) = withSession("detail", admin = true) { implicit request => implicit td =>
    try {
      Future.successful(Ok(views.html.admin.graphql.queryDetail(request.identity, path, files.read(path))))
    } catch {
      case NonFatal(x) => Future.successful(Ok(views.html.admin.graphql.queryDetail(request.identity, path, GraphQLRequest("Parse Error"), Some(x))))
    }
  }

  def queryForm(path: String) = withSession("form", admin = true) { implicit request => implicit td =>
    try {
      Future.successful(Ok(views.html.admin.graphql.queryForm(request.identity, files.read(path))))
    } catch {
      case NonFatal(x) => Future.successful(Ok(views.html.admin.graphql.queryForm(request.identity, GraphQLRequest("Parse Error"), Some(x))))
    }
  }

  def querySave(path: String) = withSession("form", admin = true) { implicit request => implicit td =>
    Future.successful(Redirect(controllers.admin.graphql.routes.GraphQLRepositoryController.queryDetail(path)))
  }

  protected def listResponse(path: Option[String])(implicit request: Req, td: TraceData) = {
    val files = FileRepository.list(key, path)
    Future.successful(render {
      case Accepts.Html() => Ok(views.html.admin.graphql.queryList(request.identity, path, files._1, files._2))
      case Accepts.Json() => Ok(Json.obj(
        "path" -> (key + path.map("/" + _).getOrElse("")).asJson,
        "directories" -> files._1.asJson,
        "files" -> files._2.asJson
      ).spaces2).as(JSON)
    })
  }

}
