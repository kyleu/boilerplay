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
import models.film.{FilmActorRow, FilmActorRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.film.FilmActorRowService

@javax.inject.Singleton
class FilmActorRowController @javax.inject.Inject() (
    override val app: Application, svc: FilmActorRowService, noteSvc: NoteService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("film", "FilmActorRow", "Film Actor", Some(models.template.Icons.filmActorRow), "view", "edit")

  def createForm = withSession("create.form", ("film", "FilmActorRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.FilmActorRowController.list()
    val call = controllers.admin.film.routes.FilmActorRowController.create()
    Future.successful(Ok(views.html.admin.film.filmActorRowForm(
      app.cfg(u = Some(request.identity), "film", "film_actor", "Create"), FilmActorRow.empty(), "New Film Actor", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("film", "FilmActorRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.film.routes.FilmActorRowController.view(model.actorId, model.filmId))
      case None => Redirect(controllers.admin.film.routes.FilmActorRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("film", "FilmActorRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.film.routes.FilmActorRowController.view(model.actorId, model.filmId))
          case _ => Ok(views.html.admin.film.filmActorRowList(app.cfg(u = Some(request.identity), "film", "film_actor"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(FilmActorRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FilmActorRow", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("film", "FilmActorRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def byFilmId(filmId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.filmId", ("film", "FilmActorRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByFilmId(request, filmId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film_actor", "Film Id")
          val list = views.html.admin.film.filmActorRowByFilmId(cfg, filmId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Film Actors by Film Id [$filmId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FilmActorRow by filmId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byActorId(actorId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.actorId", ("film", "FilmActorRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByActorId(request, actorId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "film", "film_actor", "Actor Id")
          val list = views.html.admin.film.filmActorRowByActorId(cfg, actorId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Film Actors by Actor Id [$actorId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("FilmActorRow by actorId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(actorId: Int, filmId: Int, t: Option[String] = None) = withSession("view", ("film", "FilmActorRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, actorId, filmId)
    val notesF = noteSvc.getFor(request, "FilmActorRow", actorId, filmId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.film.filmActorRowView(app.cfg(u = Some(request.identity), "film", "film_actor", s"${model.actorId}, ${model.filmId}"), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No FilmActorRow found with actorId, filmId [$actorId, $filmId]")
    })
  }

  def editForm(actorId: Int, filmId: Int) = withSession("edit.form", ("film", "FilmActorRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.FilmActorRowController.view(actorId, filmId)
    val call = controllers.admin.film.routes.FilmActorRowController.edit(actorId, filmId)
    svc.getByPrimaryKey(request, actorId, filmId).map {
      case Some(model) => Ok(
        views.html.admin.film.filmActorRowForm(app.cfg(Some(request.identity), "film", "film_actor", "Edit"), model, s"Film Actor [$actorId, $filmId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No FilmActorRow found with actorId, filmId [$actorId, $filmId]")
    }
  }

  def edit(actorId: Int, filmId: Int) = withSession("edit", ("film", "FilmActorRow", "edit")) { implicit request => implicit td =>
    svc.update(request, actorId = actorId, filmId = filmId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.FilmActorRowController.view(res._1.actorId, res._1.filmId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(actorId: Int, filmId: Int) = withSession("remove", ("film", "FilmActorRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, actorId = actorId, filmId = filmId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.FilmActorRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
