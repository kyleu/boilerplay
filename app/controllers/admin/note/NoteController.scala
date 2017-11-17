package controllers.admin.note

import controllers.BaseController
import io.circe.syntax._
import java.util.UUID
import models.Application
import models.note.NoteResult
import models.result.orderBy.OrderBy
import services.audit.AuditRoutes

import scala.concurrent.Future
import services.note.NoteService
import util.FutureUtils.defaultContext
import util.web.ControllerUtils.acceptsCsv

@javax.inject.Singleton
class NoteController @javax.inject.Inject() (override val app: Application, svc: NoteService) extends BaseController("note") {
  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.note.routes.NoteController.list()
    val call = controllers.admin.note.routes.NoteController.create()
    Future.successful(Ok(views.html.admin.note.noteForm(
      request.identity, models.note.Note.empty(), "New Note", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body.asFormUrlEncoded)).map {
      case Some(note) => Redirect(note.relType match {
        case Some(model) => AuditRoutes.getViewRoute(model, note.relPk.map(_.split("/")).getOrElse(Array.empty[String]).toSeq)
        case None => controllers.admin.note.routes.NoteController.view(note.id)
      }).flashing("success" -> s"Note [${note.id}] saved succesfully")
      case None => Redirect(controllers.admin.note.routes.NoteController.list()).flashing("success" -> "Note saved succesfully.")
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => svc.searchWithCount(request, query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => svc.getAllWithCount(request, Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      f.map(r => render {
        case Accepts.Html() => Ok(views.html.admin.note.noteList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(NoteResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson.spaces2).as(JSON)
        case acceptsCsv() => Ok(svc.csvFor("Note", r._1, r._2)).as("text/csv")
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => svc.search(request, query, Nil, orderBys, limit.orElse(Some(5)), None)
        case _ => svc.getAll(request, Nil, orderBys, limit.orElse(Some(5)))
      }
      f.map(r => Ok(r.map(_.toSummary).asJson.spaces2).as(JSON))
    }
  }

  def byAuthor(author: UUID, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("get.by.author", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByAuthor(request, author, orderBys, limit, offset).map(models => render {
        case Accepts.Html() => Ok(views.html.admin.note.noteByAuthor(
          request.identity, author, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(models.asJson.spaces2).as(JSON)
        case acceptsCsv() => Ok(svc.csvFor("Note by author", 0, models)).as("text/csv")
      })
    }
  }

  def view(id: java.util.UUID) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = app.noteService.getFor("note", id)
    val auditsF = app.auditRecordService.getByModel(request, "note", id)

    notesF.flatMap(notes => auditsF.flatMap(audits => modelF.map {
      case Some(model) => render {
        case Accepts.Html() => Ok(views.html.admin.note.noteView(request.identity, model, notes, audits, app.config.debug))
        case Accepts.Json() => Ok(model.asJson.spaces2).as(JSON)
      }
      case None => NotFound(s"No Note found with id [$id].")
    }))
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
    val fields = modelForm(request.body.asFormUrlEncoded)
    svc.update(request, id = id, fields = fields).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.note.routes.NoteController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson.spaces2).as(JSON)
    })
  }

  def remove(id: java.util.UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.note.routes.NoteController.list())
      case Accepts.Json() => Ok("{ \"status\": \"removed\" }").as(JSON)
    })
  }
}
