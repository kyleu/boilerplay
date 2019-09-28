/* Generated File */
package controllers.admin.film

import com.kyleu.projectile.controllers.{BaseController, ServiceAuthController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ControllerUtils
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.{Credentials, DateUtils}
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.film.{FilmRow, FilmRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.LanguageRowService
import services.film.{FilmActorRowService, FilmCategoryRowService, FilmRowService}
import services.store.InventoryRowService

@javax.inject.Singleton
class FilmRowController @javax.inject.Inject() (
    override val app: Application, svc: FilmRowService, noteSvc: NoteService,
    inventoryRowS: InventoryRowService, filmCategoryRowS: FilmCategoryRowService, filmActorRowS: FilmActorRowService, languageRowS: LanguageRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("film", "FilmRow", "Film", Some(models.template.Icons.filmRow), "view", "edit")
  private[this] val defaultOrderBy = Some("title" -> true)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("film", "FilmRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.film.routes.FilmRowController.view(model.filmId))
          case _ => Ok(views.html.admin.film.filmRowList(app.cfg(u = Some(request.identity), "film", "film"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(FilmRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("FilmRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("film", "FilmRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(filmId: Int, t: Option[String] = None) = withSession("view", ("film", "FilmRow", "view")) { implicit request => implicit td =>
    val creds: Credentials = request
    val modelF = svc.getByPrimaryKeyRequired(creds, filmId)
    val notesF = noteSvc.getFor(creds, "FilmRow", filmId)
    val languageIdF = modelF.flatMap(m => languageRowS.getByPrimaryKey(creds, m.languageId))
    val originalLanguageIdF = modelF.flatMap(m => m.originalLanguageId.map(v => languageRowS.getByPrimaryKey(creds, v)).getOrElse(Future.successful(None)))

    languageIdF.flatMap(languageIdR => originalLanguageIdF.flatMap(originalLanguageIdR =>
      notesF.flatMap(notes => modelF.map { model =>
        renderChoice(t) {
          case MimeTypes.HTML => Ok(views.html.admin.film.filmRowView(app.cfg(u = Some(request.identity), "film", "film", model.filmId.toString), model, notes, languageIdR, originalLanguageIdR, app.config.debug))
          case MimeTypes.JSON => Ok(model.asJson)
        }
      })))
  }

  def editForm(filmId: Int) = withSession("edit.form", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.FilmRowController.view(filmId)
    val call = controllers.admin.film.routes.FilmRowController.edit(filmId)
    svc.getByPrimaryKey(request, filmId).map {
      case Some(model) => Ok(
        views.html.admin.film.filmRowForm(app.cfg(Some(request.identity), "film", "film", "Edit"), model, s"Film [$filmId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No FilmRow found with filmId [$filmId]")
    }
  }

  def edit(filmId: Int) = withSession("edit", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    svc.update(request, filmId = filmId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.FilmRowController.view(res._1.filmId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(filmId: Int) = withSession("remove", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, filmId = filmId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.FilmRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.FilmRowController.list()
    val call = controllers.admin.film.routes.FilmRowController.create()
    Future.successful(Ok(views.html.admin.film.filmRowForm(
      app.cfg(u = Some(request.identity), "film", "film", "Create"), FilmRow.empty(), "New Film", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.film.routes.FilmRowController.view(model.filmId))
      case None => Redirect(controllers.admin.film.routes.FilmRowController.list())
    }
  }

  def bulkEditForm = withSession("bulk.edit.form", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    val act = controllers.admin.film.routes.FilmRowController.bulkEdit()
    Future.successful(Ok(views.html.admin.film.filmRowBulkForm(app.cfg(Some(request.identity), "film", "film", "Bulk Edit"), Nil, act, debug = app.config.debug)))
  }
  def bulkEdit = withSession("bulk.edit", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val typed = pks.map(pk => pk.head.toInt)
    val changes = modelForm(request.body)
    svc.updateBulk(request, typed, changes).map(msg => Ok("OK: " + msg))
  }

  def byLanguageId(languageId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.languageId", ("film", "FilmRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByLanguageId(request, languageId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film", "Language Id")
          val list = views.html.admin.film.filmRowByLanguageId(cfg, languageId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Films by Language Id [$languageId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("FilmRow by languageId", svc.csvFor(0, models))
      })
    }
  }

  def byLanguageIdBulkForm(languageId: Int) = {
    withSession("get.by.languageId", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
      svc.getByLanguageId(request, languageId).map { modelSeq =>
        val act = controllers.admin.film.routes.FilmRowController.bulkEdit()
        Ok(views.html.admin.film.filmRowBulkForm(app.cfg(Some(request.identity), "film", "film", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }

  def byOriginalLanguageId(originalLanguageId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.originalLanguageId", ("film", "FilmRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByOriginalLanguageId(request, originalLanguageId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film", "Original Language Id")
          val list = views.html.admin.film.filmRowByOriginalLanguageId(cfg, originalLanguageId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Films by Original Language Id [$originalLanguageId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("FilmRow by originalLanguageId", svc.csvFor(0, models))
      })
    }
  }

  def byOriginalLanguageIdBulkForm(originalLanguageId: Int) = {
    withSession("get.by.originalLanguageId", ("film", "FilmRow", "edit")) { implicit request => implicit td =>
      svc.getByOriginalLanguageId(request, originalLanguageId).map { modelSeq =>
        val act = controllers.admin.film.routes.FilmRowController.bulkEdit()
        Ok(views.html.admin.film.filmRowBulkForm(app.cfg(Some(request.identity), "film", "film", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
  def relationCounts(filmId: Int) = withSession("relation.counts", ("film", "FilmRow", "view")) { implicit request => implicit td =>
    val filmActorRowByFilmIdF = filmActorRowS.countByFilmId(request, filmId)
    val filmCategoryRowByFilmIdF = filmCategoryRowS.countByFilmId(request, filmId)
    val inventoryRowByFilmIdF = inventoryRowS.countByFilmId(request, filmId)
    for (filmActorRowByFilmIdC <- filmActorRowByFilmIdF; filmCategoryRowByFilmIdC <- filmCategoryRowByFilmIdF; inventoryRowByFilmIdC <- inventoryRowByFilmIdF) yield {
      Ok(Seq(
        RelationCount(model = "filmActorRow", field = "filmId", count = filmActorRowByFilmIdC),
        RelationCount(model = "filmCategoryRow", field = "filmId", count = filmCategoryRowByFilmIdC),
        RelationCount(model = "inventoryRow", field = "filmId", count = inventoryRowByFilmIdC)
      ).asJson)
    }
  }
}
