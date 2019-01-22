package controllers.admin.graphql

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import graphql.Schema

import scala.concurrent.ExecutionContext.Implicits.global
import sangria.renderer.SchemaRenderer

import scala.concurrent.Future

@javax.inject.Singleton
class SchemaController @javax.inject.Inject() (override val app: Application) extends AuthController("schema") {
  lazy val idl = SchemaRenderer.renderSchema(Schema.schema)

  def renderSchema() = withSession("graphql.schema", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(idl))
  }

  def voyager(root: String) = withSession("schema.render", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.graphql.voyager(request.identity, root = root)))
  }
}
