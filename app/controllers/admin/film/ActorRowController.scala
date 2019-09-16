/* Generated File */
package controllers.admin.film

import com.kyleu.projectile.controllers.{BaseController, ServiceAuthController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import models.film.{ActorRow, ActorRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.film.{ActorRowService, FilmActorRowService}

@javax.inject.Singleton
class ActorRowController @javax.inject.Inject() (
    override val app: Application, svc: ActorRowService, noteSvc: NoteService,
    filmActorRowS: FilmActorRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("film", "ActorRow", "Actor", Some(models.template.Icons.actorRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastName" -> true)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("film", "ActorRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.film.routes.ActorRowController.view(model.actorId))
          case _ => Ok(views.html.admin.film.actorRowList(app.cfg(u = Some(request.identity), "film", "actor"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(ActorRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("ActorRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("film", "ActorRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(actorId: Int, t: Option[String] = None) = withSession("view", ("film", "ActorRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, actorId)
    val notesF = noteSvc.getFor(request, "ActorRow", actorId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.film.actorRowView(app.cfg(u = Some(request.identity), "film", "actor", model.actorId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
      }
      case None => NotFound(s"No ActorRow found with actorId [$actorId]")
    })
  }

  def editForm(actorId: Int) = withSession("edit.form", ("film", "ActorRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.ActorRowController.view(actorId)
    val call = controllers.admin.film.routes.ActorRowController.edit(actorId)
    svc.getByPrimaryKey(request, actorId).map {
      case Some(model) => Ok(
        views.html.admin.film.actorRowForm(app.cfg(Some(request.identity), "film", "actor", "Edit"), model, s"Actor [$actorId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No ActorRow found with actorId [$actorId]")
    }
  }

  def edit(actorId: Int) = withSession("edit", ("film", "ActorRow", "edit")) { implicit request => implicit td =>
    svc.update(request, actorId = actorId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.ActorRowController.view(res._1.actorId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(actorId: Int) = withSession("remove", ("film", "ActorRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, actorId = actorId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.ActorRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("film", "ActorRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.ActorRowController.list()
    val call = controllers.admin.film.routes.ActorRowController.create()
    Future.successful(Ok(views.html.admin.film.actorRowForm(
      app.cfg(u = Some(request.identity), "film", "actor", "Create"), ActorRow.empty(), "New Actor", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("film", "ActorRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.film.routes.ActorRowController.view(model.actorId))
      case None => Redirect(controllers.admin.film.routes.ActorRowController.list())
    }
  }

  def relationCounts(actorId: Int) = withSession("relation.counts", ("film", "ActorRow", "view")) { implicit request => implicit td =>
    val filmActorRowByActorIdF = filmActorRowS.countByActorId(request, actorId)
    for (filmActorRowByActorIdC <- filmActorRowByActorIdF) yield {
      Ok(Seq(
        RelationCount(model = "filmActorRow", field = "actorId", count = filmActorRowByActorIdC)
      ).asJson)
    }
  }
}
