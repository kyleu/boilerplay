/* Generated File */
package controllers.admin.task

import controllers.admin.ServiceController
import models.Application
import models.result.orderBy.OrderBy
import models.task.ScheduledTaskRunResult
import play.api.http.MimeTypes
import scala.concurrent.Future
import services.audit.AuditRecordService
import services.task.ScheduledTaskRunService
import util.JsonSerializers._
import util.ReftreeUtils._

@javax.inject.Singleton
class ScheduledTaskRunController @javax.inject.Inject() (
    override val app: Application, svc: ScheduledTaskRunService, auditRecordSvc: AuditRecordService
) extends ServiceController(svc) {
  import app.contexts.webContext

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.task.routes.ScheduledTaskRunController.list()
    val call = controllers.admin.task.routes.ScheduledTaskRunController.create()
    Future.successful(Ok(views.html.admin.task.scheduledTaskRunForm(
      request.identity, models.task.ScheduledTaskRun.empty(), "New Scheduled Task Run", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.task.routes.ScheduledTaskRunController.view(model.id))
      case None => Redirect(controllers.admin.task.routes.ScheduledTaskRunController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.task.scheduledTaskRunList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(ScheduledTaskRunResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("ScheduledTaskRun", svc.csvFor(r._1, r._2))
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

  def view(id: java.util.UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = app.coreServices.notes.getFor(request, "scheduledTaskRun", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.task.scheduledTaskRunView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No ScheduledTaskRun found with id [$id].")
    })
  }

  def editForm(id: java.util.UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.task.routes.ScheduledTaskRunController.view(id)
    val call = controllers.admin.task.routes.ScheduledTaskRunController.edit(id)
    svc.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(
        views.html.admin.task.scheduledTaskRunForm(request.identity, model, s"Scheduled Task Run [$id]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No ScheduledTaskRun found with id [$id].")
    }
  }

  def edit(id: java.util.UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, id = id, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.task.routes.ScheduledTaskRunController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(id: java.util.UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.task.routes.ScheduledTaskRunController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
