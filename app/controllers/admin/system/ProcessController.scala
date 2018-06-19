package controllers.admin.system

import java.util.UUID

import controllers.BaseController
import graphql.GraphQLService
import models.Application
import services.ServiceRegistry
import services.process.ProcessService

import scala.concurrent.Future

@javax.inject.Singleton
class ProcessController @javax.inject.Inject() (
    override val app: Application, services: ServiceRegistry, graphQLService: GraphQLService
) extends BaseController("process") {
  import app.contexts.webContext

  def list = withSession("sandbox.list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.process.procList(request.identity, ProcessService.getActive)))
  }

  def run(cmd: Option[String]) = withSession("run", admin = true) { implicit request => implicit td =>
    val cmdSplit = cmd.getOrElse("").split(' ').filter(_.nonEmpty)
    if (cmdSplit.isEmpty) {
      throw new IllegalStateException("Please provide a command to run by passing the \"cmd\" query string parameter.")
    }
    val proc = ProcessService.start(request, cmdSplit, o => println(o), (e, d) => println(d + ": " + e))
    Future.successful(render {
      case Accepts.Html() => Ok(views.html.admin.process.procDetail(request.identity, proc))
      //case Accepts.Json() => Ok(proc.asJson)
    })
  }

  def detail(id: UUID) = withSession("list", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.process.procDetail(request.identity, ProcessService.getProc(id))))
  }
}
