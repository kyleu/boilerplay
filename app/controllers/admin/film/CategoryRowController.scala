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
import models.film.{CategoryRow, CategoryRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.film.{CategoryRowService, FilmCategoryRowService}

@javax.inject.Singleton
class CategoryRowController @javax.inject.Inject() (
    override val app: Application, svc: CategoryRowService, noteSvc: NoteService,
    filmCategoryRowS: FilmCategoryRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("film", "CategoryRow", "Category", Some(models.template.Icons.categoryRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastUpdate" -> false)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("film", "CategoryRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.film.routes.CategoryRowController.view(model.categoryId))
          case _ => Ok(views.html.admin.film.categoryRowList(app.cfg(u = Some(request.identity), "film", "category"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(CategoryRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("CategoryRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("film", "CategoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(categoryId: Int, t: Option[String] = None) = withSession("view", ("film", "CategoryRow", "view")) { implicit request => implicit td =>
    val creds: Credentials = request
    val modelF = svc.getByPrimaryKeyRequired(creds, categoryId)
    val notesF = noteSvc.getFor(creds, "CategoryRow", categoryId)

    notesF.flatMap(notes => modelF.map { model =>
      renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.film.categoryRowView(app.cfg(u = Some(request.identity), "film", "category", model.categoryId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
      }
    })
  }

  def editForm(categoryId: Int) = withSession("edit.form", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.CategoryRowController.view(categoryId)
    val call = controllers.admin.film.routes.CategoryRowController.edit(categoryId)
    svc.getByPrimaryKey(request, categoryId).map {
      case Some(model) => Ok(
        views.html.admin.film.categoryRowForm(app.cfg(Some(request.identity), "film", "category", "Edit"), model, s"Category [$categoryId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No CategoryRow found with categoryId [$categoryId]")
    }
  }

  def edit(categoryId: Int) = withSession("edit", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    svc.update(request, categoryId = categoryId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.CategoryRowController.view(res._1.categoryId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(categoryId: Int) = withSession("remove", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, categoryId = categoryId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.film.routes.CategoryRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.film.routes.CategoryRowController.list()
    val call = controllers.admin.film.routes.CategoryRowController.create()
    Future.successful(Ok(views.html.admin.film.categoryRowForm(
      app.cfg(u = Some(request.identity), "film", "category", "Create"), CategoryRow.empty(), "New Category", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.film.routes.CategoryRowController.view(model.categoryId))
      case None => Redirect(controllers.admin.film.routes.CategoryRowController.list())
    }
  }

  def bulkEditForm = withSession("bulk.edit.form", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    val act = controllers.admin.film.routes.CategoryRowController.bulkEdit()
    Future.successful(Ok(views.html.admin.film.categoryRowBulkForm(app.cfg(Some(request.identity), "film", "category", "Bulk Edit"), Nil, act, debug = app.config.debug)))
  }
  def bulkEdit = withSession("bulk.edit", ("film", "CategoryRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val typed = pks.map(pk => pk.head.toInt)
    val changes = modelForm(request.body)
    svc.updateBulk(request, typed, changes).map(msg => Ok("OK: " + msg))
  }
  def relationCounts(categoryId: Int) = withSession("relation.counts", ("film", "CategoryRow", "view")) { implicit request => implicit td =>
    val filmCategoryRowByCategoryIdF = filmCategoryRowS.countByCategoryId(request, categoryId)
    for (filmCategoryRowByCategoryIdC <- filmCategoryRowByCategoryIdF) yield {
      Ok(Seq(
        RelationCount(model = "filmCategoryRow", field = "categoryId", count = filmCategoryRowByCategoryIdC)
      ).asJson)
    }
  }
}
