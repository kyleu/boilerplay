/* Generated File */
package controllers.admin.store

import com.kyleu.projectile.controllers.{BaseController, ServiceAuthController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ReftreeUtils._
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.store.{StoreRow, StoreRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.CustomerRowService
import services.store.{InventoryRowService, StaffRowService, StoreRowService}

@javax.inject.Singleton
class StoreRowController @javax.inject.Inject() (
    override val app: Application, svc: StoreRowService, noteSvc: NoteService,
    customerRowS: CustomerRowService, staffRowS: StaffRowService, inventoryRowS: InventoryRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("store", "StoreRow", "Store", Some(models.template.Icons.storeRow), "view", "edit")

  def createForm = withSession("create.form", ("store", "StoreRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.store.routes.StoreRowController.list()
    val call = controllers.admin.store.routes.StoreRowController.create()
    Future.successful(Ok(views.html.admin.store.storeRowForm(
      app.cfg(u = Some(request.identity), "store", "store", "Create"), StoreRow.empty(), "New Store", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("store", "StoreRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.store.routes.StoreRowController.view(model.storeId))
      case None => Redirect(controllers.admin.store.routes.StoreRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("store", "StoreRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.store.routes.StoreRowController.view(model.storeId))
          case _ => Ok(views.html.admin.store.storeRowList(app.cfg(u = Some(request.identity), "store", "store"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(StoreRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("StoreRow", svc.csvFor(r._1, r._2))
        case BaseController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(BaseController.MimeTypes.png)
        case BaseController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(BaseController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("store", "StoreRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def byAddressId(addressId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.addressId", ("store", "StoreRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByAddressId(request, addressId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "store", "Address Id")
          val list = views.html.admin.store.storeRowByAddressId(cfg, addressId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Stores by Address Id [$addressId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("StoreRow by addressId", svc.csvFor(0, models))
        case BaseController.MimeTypes.png => Ok(renderToPng(v = models)).as(BaseController.MimeTypes.png)
        case BaseController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(BaseController.MimeTypes.svg)
      })
    }
  }

  def view(storeId: Int, t: Option[String] = None) = withSession("view", ("store", "StoreRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, storeId)
    val notesF = noteSvc.getFor(request, "StoreRow", storeId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.store.storeRowView(app.cfg(u = Some(request.identity), "store", "store", model.storeId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case BaseController.MimeTypes.png => Ok(renderToPng(v = model)).as(BaseController.MimeTypes.png)
        case BaseController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(BaseController.MimeTypes.svg)
      }
      case None => NotFound(s"No StoreRow found with storeId [$storeId]")
    })
  }

  def editForm(storeId: Int) = withSession("edit.form", ("store", "StoreRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.store.routes.StoreRowController.view(storeId)
    val call = controllers.admin.store.routes.StoreRowController.edit(storeId)
    svc.getByPrimaryKey(request, storeId).map {
      case Some(model) => Ok(
        views.html.admin.store.storeRowForm(app.cfg(Some(request.identity), "store", "store", "Edit"), model, s"Store [$storeId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No StoreRow found with storeId [$storeId]")
    }
  }

  def edit(storeId: Int) = withSession("edit", ("store", "StoreRow", "edit")) { implicit request => implicit td =>
    svc.update(request, storeId = storeId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.store.routes.StoreRowController.view(res._1.storeId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(storeId: Int) = withSession("remove", ("store", "StoreRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, storeId = storeId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.store.routes.StoreRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }

  def relationCounts(storeId: Int) = withSession("relation.counts", ("store", "StoreRow", "view")) { implicit request => implicit td =>
    val customerRowByStoreIdF = customerRowS.countByStoreId(request, storeId)
    val inventoryRowByStoreIdF = inventoryRowS.countByStoreId(request, storeId)
    val staffRowByStoreIdF = staffRowS.countByStoreId(request, storeId)
    for (customerRowByStoreIdC <- customerRowByStoreIdF; inventoryRowByStoreIdC <- inventoryRowByStoreIdF; staffRowByStoreIdC <- staffRowByStoreIdF) yield {
      Ok(Seq(
        RelationCount(model = "customerRow", field = "storeId", count = customerRowByStoreIdC),
        RelationCount(model = "inventoryRow", field = "storeId", count = inventoryRowByStoreIdC),
        RelationCount(model = "staffRow", field = "storeId", count = staffRowByStoreIdC)
      ).asJson)
    }
  }
}
