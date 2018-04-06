package controllers.admin.note

import java.util.UUID

import controllers.admin.ServiceController
import models.Application
import models.note.NoteResult
import models.result.orderBy.OrderBy
import play.api.http.MimeTypes
import services.audit.{AuditRecordService, AuditRoutes}

import scala.concurrent.Future
import services.note.NoteService
import util.FutureUtils.defaultContext
import util.JsonSerializers._
import util.ReftreeUtils._

@javax.inject.Singleton
class NoteController @javax.inject.Inject() (override val app: Application, svc: NoteService, auditRecordS: AuditRecordService) extends ServiceController(svc) {
  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.note.routes.NoteController.list()
    val call = controllers.admin.note.routes.NoteController.create()
    Future.successful(Ok(views.html.admin.note.noteForm(
      request.identity, models.note.Note.empty(), "New Note", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(note) => Redirect(note.relType match {
        case Some(model) => AuditRoutes.getViewRoute(model, note.relPk.map(_.split("/")).getOrElse(Array.empty[String]).toIndexedSeq)
        case None => controllers.admin.note.routes.NoteController.view(note.id)
      }).flashing("success" -> s"Note [${note.id}] saved succesfully")
      case None => Redirect(controllers.admin.note.routes.NoteController.list()).flashing("success" -> "Note saved succesfully.")
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case ServiceController.MimeTypes.csv => csvResponse("Note", svc.csvFor(r._1, r._2)).as("text/csv")
        case MimeTypes.HTML => Ok(views.html.admin.note.noteList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(NoteResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
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

  def byAuthor(author: UUID, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("get.by.author", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByAuthor(request, author, orderBys, limit, offset).map(models => renderChoice(t) {
        case ServiceController.MimeTypes.csv => csvResponse("Note by author", svc.csvFor(0, models)).as("text/csv")
        case MimeTypes.HTML => Ok(views.html.admin.note.noteByAuthor(
          request.identity, author, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(id: java.util.UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = app.coreServices.notes.getFor(request, "note", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.note.noteView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No Note found with id [$id].")
    })
  }

  def editForm(id: java.util.UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.note.routes.NoteController.view(id)
    val call = controllers.admin.note.routes.NoteController.edit(id)
    svc.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(views.html.admin.note.noteForm(request.identity, model, s"Note [$id]", cancel, call, debug = app.config.debug))
      case None => NotFound(s"No Note found with id [$id].")
    }
  }

  def edit(id: java.util.UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, id = id, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.note.routes.NoteController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(id: java.util.UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.note.routes.NoteController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
