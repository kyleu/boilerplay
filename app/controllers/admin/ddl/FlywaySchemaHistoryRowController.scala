/* Generated File */
package controllers.admin.ddl

import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import controllers.admin.ServiceController
import models.Application
import models.ddl.{FlywaySchemaHistoryRow, FlywaySchemaHistoryRowResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import services.audit.AuditRecordRowService
import services.ddl.FlywaySchemaHistoryRowService

@javax.inject.Singleton
class FlywaySchemaHistoryRowController @javax.inject.Inject() (
    override val app: Application, svc: FlywaySchemaHistoryRowService, auditRecordSvc: AuditRecordRowService
) extends ServiceController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.list()
    val call = controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.create()
    Future.successful(Ok(views.html.admin.ddl.flywaySchemaHistoryRowForm(
      request.identity, FlywaySchemaHistoryRow.empty(), "New Flyway Schema History", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.view(model.installedRank))
      case None => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.ddl.flywaySchemaHistoryRowList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(FlywaySchemaHistoryRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FlywaySchemaHistoryRow", svc.csvFor(r._1, r._2))
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
    val notesF = app.coreServices.notes.getFor(request, "flywaySchemaHistoryRow", installedRank)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.ddl.flywaySchemaHistoryRowView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No FlywaySchemaHistoryRow found with installedRank [$installedRank].")
    })
  }

  def editForm(installedRank: Long) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.view(installedRank)
    val call = controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.edit(installedRank)
    svc.getByPrimaryKey(request, installedRank).map {
      case Some(model) => Ok(
        views.html.admin.ddl.flywaySchemaHistoryRowForm(request.identity, model, s"Flyway Schema History [$installedRank]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No FlywaySchemaHistoryRow found with installedRank [$installedRank].")
    }
  }

  def edit(installedRank: Long) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, installedRank = installedRank, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.view(res._1.installedRank)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(installedRank: Long) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, installedRank = installedRank).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
