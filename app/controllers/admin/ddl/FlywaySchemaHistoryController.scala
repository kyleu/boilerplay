/* Generated File */
package controllers.admin.ddl

import controllers.admin.ServiceController
import models.Application
import models.ddl.{FlywaySchemaHistory, FlywaySchemaHistoryResult}
import models.result.orderBy.OrderBy
import play.api.http.MimeTypes
import scala.concurrent.Future
import services.audit.AuditRecordService
import services.ddl.FlywaySchemaHistoryService
import util.DateUtils
import util.JsonSerializers._
import util.ReftreeUtils._

@javax.inject.Singleton
class FlywaySchemaHistoryController @javax.inject.Inject() (
    override val app: Application, svc: FlywaySchemaHistoryService, auditRecordSvc: AuditRecordService
) extends ServiceController(svc) {
  import app.contexts.webContext

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.ddl.routes.FlywaySchemaHistoryController.list()
    val call = controllers.admin.ddl.routes.FlywaySchemaHistoryController.create()
    Future.successful(Ok(views.html.admin.ddl.flywaySchemaHistoryForm(
      request.identity, FlywaySchemaHistory.empty(), "New Flyway Schema History", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryController.view(model.installedRank))
      case None => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.ddl.flywaySchemaHistoryList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(FlywaySchemaHistoryResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FlywaySchemaHistory", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(installedRank: Long, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, installedRank)
    val notesF = app.coreServices.notes.getFor(request, "flywaySchemaHistory", installedRank)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.ddl.flywaySchemaHistoryView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No FlywaySchemaHistory found with installedRank [$installedRank].")
    })
  }

  def editForm(installedRank: Long) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.ddl.routes.FlywaySchemaHistoryController.view(installedRank)
    val call = controllers.admin.ddl.routes.FlywaySchemaHistoryController.edit(installedRank)
    svc.getByPrimaryKey(request, installedRank).map {
      case Some(model) => Ok(
        views.html.admin.ddl.flywaySchemaHistoryForm(request.identity, model, s"Flyway Schema History [$installedRank]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No FlywaySchemaHistory found with installedRank [$installedRank].")
    }
  }

  def edit(installedRank: Long) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, installedRank = installedRank, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryController.view(res._1.installedRank)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(installedRank: Long) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, installedRank = installedRank).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
