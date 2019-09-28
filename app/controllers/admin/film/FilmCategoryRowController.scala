/* Generated File */
package controllers.admin.film

import com.kyleu.projectile.controllers.{BaseController, ServiceAuthController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ControllerUtils
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.{Credentials, DateUtils}
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.film.{FilmCategoryRow, FilmCategoryRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.film.{CategoryRowService, FilmCategoryRowService, FilmRowService}

@javax.inject.Singleton
class FilmCategoryRowController @javax.inject.Inject() (
    override val app: Application, svc: FilmCategoryRowService, noteSvc: NoteService,
    filmRowS: FilmRowService, categoryRowS: CategoryRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("film", "FilmCategoryRow", "Film Category", Some(models.template.Icons.filmCategoryRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastUpdate" -> false)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.film.routes.FilmCategoryRowController.view(model.filmId, model.categoryId))
          case _ => Ok(views.html.admin.film.filmCategoryRowList(app.cfg(u = Some(request.identity), "film", "film_category"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(FilmCategoryRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("FilmCategoryRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(filmId: Int, categoryId: Int, t: Option[String] = None) = withSession("view", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
    val creds: Credentials = request
    val modelF = svc.getByPrimaryKeyRequired(creds, filmId, categoryId)
    val notesF = noteSvc.getFor(creds, "FilmCategoryRow", filmId, categoryId)
    val filmIdF = modelF.flatMap(m => filmRowS.getByPrimaryKey(creds, m.filmId))
    val categoryIdF = modelF.flatMap(m => categoryRowS.getByPrimaryKey(creds, m.categoryId))

    filmIdF.flatMap(filmIdR => categoryIdF.flatMap(categoryIdR =>
      notesF.flatMap(notes => modelF.map { model =>
        renderChoice(t) {
          case MimeTypes.HTML => Ok(views.html.admin.film.filmCategoryRowView(app.cfg(u = Some(request.identity), "film", "film_category", s"${model.filmId}, ${model.categoryId}"), model, notes, filmIdR, categoryIdR, app.config.debug))
          case MimeTypes.JSON => Ok(model.asJson)
        }
      })))
  }

  def editForm(filmId: Int, categoryId: Int) = withSession("edit.form", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.FilmCategoryRowController.view(filmId, categoryId)
    val call = controllers.admin.film.routes.FilmCategoryRowController.edit(filmId, categoryId)
    svc.getByPrimaryKey(request, filmId, categoryId).map {
      case Some(model) => Ok(
        views.html.admin.film.filmCategoryRowForm(app.cfg(Some(request.identity), "film", "film_category", "Edit"), model, s"Film Category [$filmId, $categoryId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No FilmCategoryRow found with filmId, categoryId [$filmId, $categoryId]")
    }
  }

  def edit(filmId: Int, categoryId: Int) = withSession("edit", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    svc.update(request, filmId = filmId, categoryId = categoryId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.FilmCategoryRowController.view(res._1.filmId, res._1.categoryId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(filmId: Int, categoryId: Int) = withSession("remove", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, filmId = filmId, categoryId = categoryId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.FilmCategoryRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.FilmCategoryRowController.list()
    val call = controllers.admin.film.routes.FilmCategoryRowController.create()
    Future.successful(Ok(views.html.admin.film.filmCategoryRowForm(
      app.cfg(u = Some(request.identity), "film", "film_category", "Create"), FilmCategoryRow.empty(), "New Film Category", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.film.routes.FilmCategoryRowController.view(model.filmId, model.categoryId))
      case None => Redirect(controllers.admin.film.routes.FilmCategoryRowController.list())
    }
  }

  def bulkEditForm = withSession("bulk.edit.form", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    val act = controllers.admin.film.routes.FilmCategoryRowController.bulkEdit()
    Future.successful(Ok(views.html.admin.film.filmCategoryRowBulkForm(app.cfg(Some(request.identity), "film", "film_category", "Bulk Edit"), Nil, act, debug = app.config.debug)))
  }
  def bulkEdit = withSession("bulk.edit", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val typed = pks.map(pk => (pk.head.toInt, pk(1).toInt))
    val changes = modelForm(request.body)
    svc.updateBulk(request, typed, changes).map(msg => Ok("OK: " + msg))
  }

  def byFilmId(filmId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.filmId", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByFilmId(request, filmId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film_category", "Film Id")
          val list = views.html.admin.film.filmCategoryRowByFilmId(cfg, filmId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Film Categories by Film Id [$filmId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("FilmCategoryRow by filmId", svc.csvFor(0, models))
      })
    }
  }

  def byFilmIdBulkForm(filmId: Int) = {
    withSession("get.by.filmId", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
      svc.getByFilmId(request, filmId).map { modelSeq =>
        val act = controllers.admin.film.routes.FilmCategoryRowController.bulkEdit()
        Ok(views.html.admin.film.filmCategoryRowBulkForm(app.cfg(Some(request.identity), "film", "film_category", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }

  def byCategoryId(categoryId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.categoryId", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByCategoryId(request, categoryId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film_category", "Category Id")
          val list = views.html.admin.film.filmCategoryRowByCategoryId(cfg, categoryId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Film Categories by Category Id [$categoryId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("FilmCategoryRow by categoryId", svc.csvFor(0, models))
      })
    }
  }

  def byCategoryIdBulkForm(categoryId: Int) = {
    withSession("get.by.categoryId", ("film", "FilmCategoryRow", "edit")) { implicit request => implicit td =>
      svc.getByCategoryId(request, categoryId).map { modelSeq =>
        val act = controllers.admin.film.routes.FilmCategoryRowController.bulkEdit()
        Ok(views.html.admin.film.filmCategoryRowBulkForm(app.cfg(Some(request.identity), "film", "film_category", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
}
