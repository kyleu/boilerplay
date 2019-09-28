/* Generated File */
package controllers.admin.customer

import com.kyleu.projectile.controllers.{BaseController, ServiceAuthController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ControllerUtils
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.{Credentials, DateUtils}
import com.kyleu.projectile.util.JsonSerializers._
import models.customer.{LanguageRow, LanguageRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.LanguageRowService
import services.film.FilmRowService

@javax.inject.Singleton
class LanguageRowController @javax.inject.Inject() (
    override val app: Application, svc: LanguageRowService, noteSvc: NoteService,
    filmRowS: FilmRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("customer", "LanguageRow", "Language", Some(models.template.Icons.languageRow), "view", "edit")
  private[this] val defaultOrderBy = Some("name" -> true)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("customer", "LanguageRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.customer.routes.LanguageRowController.view(model.languageId))
          case _ => Ok(views.html.admin.customer.languageRowList(app.cfg(u = Some(request.identity), "customer", "language"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(LanguageRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("LanguageRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("customer", "LanguageRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(languageId: Int, t: Option[String] = None) = withSession("view", ("customer", "LanguageRow", "view")) { implicit request => implicit td =>
    val creds: Credentials = request
    val modelF = svc.getByPrimaryKeyRequired(creds, languageId)
    val notesF = noteSvc.getFor(creds, "LanguageRow", languageId)

    notesF.flatMap(notes => modelF.map { model =>
      renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.customer.languageRowView(app.cfg(u = Some(request.identity), "customer", "language", model.languageId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
      }
    })
  }

  def editForm(languageId: Int) = withSession("edit.form", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.customer.routes.LanguageRowController.view(languageId)
    val call = controllers.admin.customer.routes.LanguageRowController.edit(languageId)
    svc.getByPrimaryKey(request, languageId).map {
      case Some(model) => Ok(
        views.html.admin.customer.languageRowForm(app.cfg(Some(request.identity), "customer", "language", "Edit"), model, s"Language [$languageId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No LanguageRow found with languageId [$languageId]")
    }
  }

  def edit(languageId: Int) = withSession("edit", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    svc.update(request, languageId = languageId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.customer.routes.LanguageRowController.view(res._1.languageId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(languageId: Int) = withSession("remove", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, languageId = languageId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.customer.routes.LanguageRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.customer.routes.LanguageRowController.list()
    val call = controllers.admin.customer.routes.LanguageRowController.create()
    Future.successful(Ok(views.html.admin.customer.languageRowForm(
      app.cfg(u = Some(request.identity), "customer", "language", "Create"), LanguageRow.empty(), "New Language", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.customer.routes.LanguageRowController.view(model.languageId))
      case None => Redirect(controllers.admin.customer.routes.LanguageRowController.list())
    }
  }

  def bulkEditForm = withSession("bulk.edit.form", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    val act = controllers.admin.customer.routes.LanguageRowController.bulkEdit()
    Future.successful(Ok(views.html.admin.customer.languageRowBulkForm(app.cfg(Some(request.identity), "customer", "language", "Bulk Edit"), Nil, act, debug = app.config.debug)))
  }
  def bulkEdit = withSession("bulk.edit", ("customer", "LanguageRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val typed = pks.map(pk => pk.head.toInt)
    val changes = modelForm(request.body)
    svc.updateBulk(request, typed, changes).map(msg => Ok("OK: " + msg))
  }
  def relationCounts(languageId: Int) = withSession("relation.counts", ("customer", "LanguageRow", "view")) { implicit request => implicit td =>
    val filmRowByLanguageIdF = filmRowS.countByLanguageId(request, languageId)
    val filmRowByOriginalLanguageIdF = filmRowS.countByOriginalLanguageId(request, languageId)
    for (filmRowByLanguageIdC <- filmRowByLanguageIdF; filmRowByOriginalLanguageIdC <- filmRowByOriginalLanguageIdF) yield {
      Ok(Seq(
        RelationCount(model = "filmRow", field = "languageId", count = filmRowByLanguageIdC),
        RelationCount(model = "filmRow", field = "originalLanguageId", count = filmRowByOriginalLanguageIdC)
      ).asJson)
    }
  }
}
