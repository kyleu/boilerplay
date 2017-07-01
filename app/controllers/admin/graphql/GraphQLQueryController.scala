package controllers.admin.graphql

import controllers.BaseController
import services.graphql.GraphQLFileService
import utils.Application

import scala.concurrent.Future

@javax.inject.Singleton
class GraphQLQueryController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def load(dir: Option[String], name: String) = withAdminSession("graphql.load") { implicit request =>
    val q = GraphQLFileService.load(dir, name)
    Future.successful(Redirect(controllers.admin.graphql.routes.GraphQLController.graphql(query = Some(q), dir = dir, name = Some(name)).url))
  }

  def save = withAdminSession("graphql.save") { implicit request =>
    val form = request.body.asFormUrlEncoded.getOrElse(throw new IllegalStateException()).flatMap(x => x._2.headOption.map(y => x._1 -> y))
    val dir = form.get("dir")
    val name = form.getOrElse("name", throw new IllegalStateException("Missing [name]."))
    val body = form.getOrElse("body", throw new IllegalStateException("Missing [body]."))
    GraphQLFileService.save(dir, name, body)
    Future.successful(Redirect(controllers.admin.graphql.routes.GraphQLController.graphql(query = Some(body), dir = dir, name = Some(name))))
  }
}
