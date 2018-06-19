package controllers.admin.system

import akka.util.Timeout
import controllers.BaseController
import graphql.GraphQLService
import models.Application
import models.sandbox.SandboxTask
import services.ServiceRegistry

import scala.concurrent.Future
import scala.concurrent.duration._
import util.JsonSerializers._

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (
    override val app: Application, services: ServiceRegistry, graphQLService: GraphQLService
) extends BaseController("sandbox") {
  import app.contexts.webContext

  implicit val timeout: Timeout = Timeout(10.seconds)

  def list = withSession("sandbox.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.sandbox.sandboxList(request.identity)))
  }

  def run(key: String, arg: Option[String]) = withSession("sandbox." + key, admin = true) { implicit request => implicit td =>
    val sandbox = SandboxTask.withNameInsensitive(key)
    sandbox.run(SandboxTask.Config(app, services, graphQLService, arg)).map { result =>
      render {
        case Accepts.Html() => Ok(views.html.admin.sandbox.sandboxRun(request.identity, result))
        case Accepts.Json() => Ok(result.asJson)
      }
    }
  }
}
