/* Generated File */
package controllers.admin.film

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ReftreeUtils._
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.film.{FilmCategoryRow, FilmCategoryRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.film.FilmCategoryRowService

@javax.inject.Singleton
class FilmCategoryRowController @javax.inject.Inject() (
    override val app: Application, svc: FilmCategoryRowService, noteSvc: NoteService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("film", "FilmCategoryRow", "Film Category", Some(models.template.Icons.filmCategoryRow), "view", "edit")

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

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.film.routes.FilmCategoryRowController.view(model.filmId, model.categoryId))
          case _ => Ok(views.html.admin.film.filmCategoryRowList(app.cfg(u = Some(request.identity), "film", "film_category"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(FilmCategoryRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FilmCategoryRow", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def byFilmId(filmId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.filmId", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByFilmId(request, filmId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film_category", "Film Id")
          val list = views.html.admin.film.filmCategoryRowByFilmId(cfg, filmId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Film Categories by Film Id [$filmId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FilmCategoryRow by filmId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byCategoryId(categoryId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.categoryId", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByCategoryId(request, categoryId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film_category", "Category Id")
          val list = views.html.admin.film.filmCategoryRowByCategoryId(cfg, categoryId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Film Categories by Category Id [$categoryId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FilmCategoryRow by categoryId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(filmId: Int, categoryId: Int, t: Option[String] = None) = withSession("view", ("film", "FilmCategoryRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, filmId, categoryId)
    val notesF = noteSvc.getFor(request, "FilmCategoryRow", filmId, categoryId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.film.filmCategoryRowView(app.cfg(u = Some(request.identity), "film", "film_category", s"${model.filmId}, ${model.categoryId}"), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No FilmCategoryRow found with filmId, categoryId [$filmId, $categoryId]")
    })
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
}
