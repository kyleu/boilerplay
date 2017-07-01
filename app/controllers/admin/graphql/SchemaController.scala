package controllers.admin.graphql

import controllers.BaseController
import models.graphql.Schema
import sangria.renderer.SchemaRenderer
import utils.Application

import scala.concurrent.Future

@javax.inject.Singleton
class SchemaController @javax.inject.Inject() (override val app: Application) extends BaseController {
  def renderSchema() = withAdminSession("graphql.schema") { implicit request =>
    Future.successful(Ok(SchemaRenderer.renderSchema(Schema.schema)))
  }

  def voyager() = withSession("schema.render") { implicit request =>
    Future.successful(Ok(views.html.admin.graphql.voyager()))
  }
}
