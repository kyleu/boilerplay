package controllers.admin.graphql

import controllers.BaseController
import models.Application
import models.graphql.Schema
import sangria.renderer.SchemaRenderer

import scala.concurrent.Future

@javax.inject.Singleton
class SchemaController @javax.inject.Inject() (override val app: Application) extends BaseController("schema") {
  import app.contexts.webContext

  lazy val idl = SchemaRenderer.renderSchema(Schema.schema)

  def renderSchema() = withSession("graphql.schema", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(idl))
  }

  def voyager() = withSession("schema.render", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.graphql.voyager(request.identity)))
  }
}
