package controllers.admin.graphql

import controllers.BaseController
import models.graphql.Schema
import sangria.renderer.SchemaRenderer
import util.Application

import scala.concurrent.Future

@javax.inject.Singleton
class SchemaController @javax.inject.Inject() (override val app: Application) extends BaseController("schema") {
  import app.contexts.webContext

  def renderSchema() = withSession("graphql.schema", admin = true) { implicit request =>
    Future.successful(Ok(SchemaRenderer.renderSchema(Schema.schema)))
  }

  def voyager() = withSession("schema.render", admin = true) { implicit request =>
    Future.successful(Ok(views.html.admin.graphql.voyager(request.identity)))
  }
}
