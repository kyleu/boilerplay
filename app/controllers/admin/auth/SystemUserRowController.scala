/* Generated File */
package controllers.admin.auth

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import java.util.UUID
import models.auth.{SystemUserRow, SystemUserRowResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import services.auth.SystemUserRowService
import services.note.NoteRowService

@javax.inject.Singleton
class SystemUserRowController @javax.inject.Inject() (
    override val app: Application, authActions: AuthActions, svc: SystemUserRowService, noteSvc: NoteService,
    noteRowS: NoteRowService
) extends ServiceAuthController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.auth.routes.SystemUserRowController.list()
    val call = controllers.admin.auth.routes.SystemUserRowController.create()
    Future.successful(Ok(views.html.admin.auth.systemUserRowForm(
      request.identity, authActions, SystemUserRow.empty(), "New System User", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.auth.routes.SystemUserRowController.view(model.id))
      case None => Redirect(controllers.admin.auth.routes.SystemUserRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.auth.systemUserRowList(
          request.identity, authActions, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(SystemUserRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("SystemUserRow", svc.csvFor(r._1, r._2))
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

  def view(id: UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = noteSvc.getFor(request, "systemUserRow", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.auth.systemUserRowView(request.identity, authActions, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No SystemUserRow found with id [$id]")
    })
  }

  def editForm(id: UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.auth.routes.SystemUserRowController.view(id)
    val call = controllers.admin.auth.routes.SystemUserRowController.edit(id)
    svc.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(
        views.html.admin.auth.systemUserRowForm(request.identity, authActions, model, s"System User [$id]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No SystemUserRow found with id [$id]")
    }
  }

  def edit(id: UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, id = id, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.auth.routes.SystemUserRowController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(id: UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.auth.routes.SystemUserRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }

  def relationCounts(id: UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val noteRowByAuthorF = noteRowS.countByAuthor(request, id)
    for (noteRowC <- noteRowByAuthorF) yield {
      Ok(Seq(
        RelationCount(model = "noteRow", field = "author", count = noteRowC)
      ).asJson)
    }
  }
}
