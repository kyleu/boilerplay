/* Generated File */
package controllers.admin.sync

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.audit.AuditService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import models.sync.{SyncProgressRow, SyncProgressRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.sync.SyncProgressRowService

@javax.inject.Singleton
class SyncProgressRowController @javax.inject.Inject() (
    override val app: Application, svc: SyncProgressRowService, noteSvc: NoteService, auditRecordSvc: AuditService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.sync.routes.SyncProgressRowController.list()
    val call = controllers.admin.sync.routes.SyncProgressRowController.create()
    Future.successful(Ok(views.html.admin.sync.syncProgressRowForm(
      app.cfg(Some(request.identity), true, "sync", "sync_progress", "Create"), SyncProgressRow.empty(), "New Sync Progress", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.sync.routes.SyncProgressRowController.view(model.key))
      case None => Redirect(controllers.admin.sync.routes.SyncProgressRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil => Redirect(controllers.admin.sync.routes.SyncProgressRowController.view(model.key))
          case _ => Ok(views.html.admin.sync.syncProgressRowList(app.cfg(u = Some(request.identity), admin = true, "sync", "sync_progress"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(SyncProgressRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("SyncProgressRow", svc.csvFor(r._1, r._2))
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

  def view(key: String, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, key)
    val auditsF = auditRecordSvc.getByModel(request, "SyncProgressRow", key)
    val notesF = noteSvc.getFor(request, "SyncProgressRow", key)

    notesF.flatMap(notes => auditsF.flatMap(audits => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.sync.syncProgressRowView(app.cfg(Some(request.identity), true, "sync", "sync_progress", model.key), model, notes, audits, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No SyncProgressRow found with key [$key]")
    }))
  }

  def editForm(key: String) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.sync.routes.SyncProgressRowController.view(key)
    val call = controllers.admin.sync.routes.SyncProgressRowController.edit(key)
    svc.getByPrimaryKey(request, key).map {
      case Some(model) => Ok(
        views.html.admin.sync.syncProgressRowForm(app.cfg(Some(request.identity), true, "sync", "sync_progress", "Edit"), model, s"Sync Progress [$key]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No SyncProgressRow found with key [$key]")
    }
  }

  def edit(key: String) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, key = key, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.sync.routes.SyncProgressRowController.view(res._1.key)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(key: String) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, key = key).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.sync.routes.SyncProgressRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
