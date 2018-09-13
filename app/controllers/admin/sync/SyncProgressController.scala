/* Generated File */
package controllers.admin.sync

import controllers.admin.ServiceController
import models.Application
import models.result.orderBy.OrderBy
import models.sync.SyncProgressResult
import play.api.http.MimeTypes
import scala.concurrent.Future
import services.audit.AuditRecordService
import services.sync.SyncProgressService
import util.JsonSerializers._
import util.ReftreeUtils._

@javax.inject.Singleton
class SyncProgressController @javax.inject.Inject() (
    override val app: Application, svc: SyncProgressService, auditRecordSvc: AuditRecordService
) extends ServiceController(svc) {
  import app.contexts.webContext

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.sync.routes.SyncProgressController.list()
    val call = controllers.admin.sync.routes.SyncProgressController.create()
    Future.successful(Ok(views.html.admin.sync.syncProgressForm(
      request.identity, models.sync.SyncProgress.empty(), "New Sync Progress", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.sync.routes.SyncProgressController.view(model.key))
      case None => Redirect(controllers.admin.sync.routes.SyncProgressController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.sync.syncProgressList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(SyncProgressResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("SyncProgress", svc.csvFor(r._1, r._2))
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
    val notesF = app.coreServices.notes.getFor(request, "syncProgress", key)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.sync.syncProgressView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No SyncProgress found with key [$key].")
    })
  }

  def editForm(key: String) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.sync.routes.SyncProgressController.view(key)
    val call = controllers.admin.sync.routes.SyncProgressController.edit(key)
    svc.getByPrimaryKey(request, key).map {
      case Some(model) => Ok(
        views.html.admin.sync.syncProgressForm(request.identity, model, s"Sync Progress [$key]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No SyncProgress found with key [$key].")
    }
  }

  def edit(key: String) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, key = key, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.sync.routes.SyncProgressController.view(res._1.key)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(key: String) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, key = key).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.sync.routes.SyncProgressController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
