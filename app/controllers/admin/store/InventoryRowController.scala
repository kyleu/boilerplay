/* Generated File */
package controllers.admin.store

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ReftreeUtils._
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.store.{InventoryRow, InventoryRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.RentalRowService
import services.store.InventoryRowService

@javax.inject.Singleton
class InventoryRowController @javax.inject.Inject() (
    override val app: Application, svc: InventoryRowService, noteSvc: NoteService,
    rentalRowS: RentalRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("store", "InventoryRow", "Inventory", Some(models.template.Icons.inventoryRow), "view", "edit")

  def createForm = withSession("create.form", ("store", "InventoryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.store.routes.InventoryRowController.list()
    val call = controllers.admin.store.routes.InventoryRowController.create()
    Future.successful(Ok(views.html.admin.store.inventoryRowForm(
      app.cfg(u = Some(request.identity), "store", "inventory", "Create"), InventoryRow.empty(), "New Inventory", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("store", "InventoryRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.store.routes.InventoryRowController.view(model.inventoryId))
      case None => Redirect(controllers.admin.store.routes.InventoryRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("store", "InventoryRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.store.routes.InventoryRowController.view(model.inventoryId))
          case _ => Ok(views.html.admin.store.inventoryRowList(app.cfg(u = Some(request.identity), "store", "inventory"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(InventoryRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("InventoryRow", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("store", "InventoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def byFilmId(filmId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.filmId", ("store", "InventoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByFilmId(request, filmId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "inventory", "Film Id")
          val list = views.html.admin.store.inventoryRowByFilmId(cfg, filmId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Inventories by Film Id [$filmId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("InventoryRow by filmId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byStoreId(storeId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.storeId", ("store", "InventoryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByStoreId(request, storeId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "inventory", "Store Id")
          val list = views.html.admin.store.inventoryRowByStoreId(cfg, storeId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Inventories by Store Id [$storeId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("InventoryRow by storeId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(inventoryId: Long, t: Option[String] = None) = withSession("view", ("store", "InventoryRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, inventoryId)
    val notesF = noteSvc.getFor(request, "InventoryRow", inventoryId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.store.inventoryRowView(app.cfg(u = Some(request.identity), "store", "inventory", model.inventoryId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No InventoryRow found with inventoryId [$inventoryId]")
    })
  }

  def editForm(inventoryId: Long) = withSession("edit.form", ("store", "InventoryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.store.routes.InventoryRowController.view(inventoryId)
    val call = controllers.admin.store.routes.InventoryRowController.edit(inventoryId)
    svc.getByPrimaryKey(request, inventoryId).map {
      case Some(model) => Ok(
        views.html.admin.store.inventoryRowForm(app.cfg(Some(request.identity), "store", "inventory", "Edit"), model, s"Inventory [$inventoryId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No InventoryRow found with inventoryId [$inventoryId]")
    }
  }

  def edit(inventoryId: Long) = withSession("edit", ("store", "InventoryRow", "edit")) { implicit request => implicit td =>
    svc.update(request, inventoryId = inventoryId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.store.routes.InventoryRowController.view(res._1.inventoryId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(inventoryId: Long) = withSession("remove", ("store", "InventoryRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, inventoryId = inventoryId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.store.routes.InventoryRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }

  def relationCounts(inventoryId: Long) = withSession("relation.counts", ("store", "InventoryRow", "view")) { implicit request => implicit td =>
    val rentalRowByInventoryIdF = rentalRowS.countByInventoryId(request, inventoryId)
    for (rentalRowByInventoryIdC <- rentalRowByInventoryIdF) yield {
      Ok(Seq(
        RelationCount(model = "rentalRow", field = "inventoryId", count = rentalRowByInventoryIdC)
      ).asJson)
    }
  }
}
