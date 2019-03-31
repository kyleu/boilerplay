/* Generated File */
package controllers.admin.audit

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.config.UiConfig
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import java.util.UUID
import models.audit.{AuditRecordRow, AuditRecordRowResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.audit.AuditRecordRowService

@javax.inject.Singleton
class AuditRecordRowController @javax.inject.Inject() (
    override val app: Application, svc: AuditRecordRowService, noteSvc: NoteService
) extends ServiceAuthController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditRecordRowController.list()
    val call = controllers.admin.audit.routes.AuditRecordRowController.create()
    Future.successful(Ok(views.html.admin.audit.auditRecordRowForm(
      request.identity, app.cfg(Some(request.identity), true, "audit", "Audit Records"), AuditRecordRow.empty(), "New Audit Record", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.audit.routes.AuditRecordRowController.view(model.id))
      case None => Redirect(controllers.admin.audit.routes.AuditRecordRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.audit.auditRecordRowList(
          request.identity, app.cfg(u = Some(request.identity), admin = true, "audit", "Audit Records"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(AuditRecordRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("AuditRecordRow", svc.csvFor(r._1, r._2))
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

  def byAuditId(auditId: UUID, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("get.by.auditId", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByAuditId(request, auditId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.audit.auditRecordRowByAuditId(
          request.identity, app.cfg(Some(request.identity), true, "audit", "Audit Records"), auditId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("AuditRecordRow by auditId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(id: UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = noteSvc.getFor(request, "auditRecordRow", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.audit.auditRecordRowView(request.identity, app.cfg(Some(request.identity), true, "audit", "Audit Records"), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No AuditRecordRow found with id [$id]")
    })
  }

  def editForm(id: UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditRecordRowController.view(id)
    val call = controllers.admin.audit.routes.AuditRecordRowController.edit(id)
    svc.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(
        views.html.admin.audit.auditRecordRowForm(request.identity, app.cfg(Some(request.identity), true, "audit", "Audit Records"), model, s"Audit Record [$id]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No AuditRecordRow found with id [$id]")
    }
  }

  def edit(id: UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, id = id, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditRecordRowController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(id: UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditRecordRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
