/* Generated File */
package controllers.admin.note

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import java.util.UUID
import models.note.{NoteRow, NoteRowResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import services.note.NoteRowService

@javax.inject.Singleton
class NoteRowController @javax.inject.Inject() (
    override val app: Application, authActions: AuthActions, svc: NoteRowService, noteSvc: NoteService
) extends ServiceAuthController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.note.routes.NoteRowController.list()
    val call = controllers.admin.note.routes.NoteRowController.create()
    Future.successful(Ok(views.html.admin.note.noteRowForm(
      request.identity, authActions, NoteRow.empty(), "New Note", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.note.routes.NoteRowController.view(model.id))
      case None => Redirect(controllers.admin.note.routes.NoteRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.note.noteRowList(
          request.identity, authActions, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(NoteRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("NoteRow", svc.csvFor(r._1, r._2))
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
        case MimeTypes.HTML => Ok(views.html.admin.note.noteRowByAuthor(
          request.identity, authActions, author, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("NoteRow by author", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(id: UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = noteSvc.getFor(request, "noteRow", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.note.noteRowView(request.identity, authActions, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No NoteRow found with id [$id]")
    })
  }

  def editForm(id: UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.note.routes.NoteRowController.view(id)
    val call = controllers.admin.note.routes.NoteRowController.edit(id)
    svc.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(
        views.html.admin.note.noteRowForm(request.identity, authActions, model, s"Note [$id]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No NoteRow found with id [$id]")
    }
  }

  def edit(id: UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, id = id, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.note.routes.NoteRowController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(id: UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.note.routes.NoteRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
