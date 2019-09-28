/* Generated File */
package controllers.admin.store

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
import models.store.{StaffRow, StaffRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.address.AddressRowService
import services.customer.RentalRowService
import services.payment.PaymentRowService
import services.store.{StaffRowService, StoreRowService}

@javax.inject.Singleton
class StaffRowController @javax.inject.Inject() (
    override val app: Application, svc: StaffRowService, noteSvc: NoteService,
    rentalRowS: RentalRowService, paymentRowS: PaymentRowService, storeRowS: StoreRowService, addressRowS: AddressRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("store", "StaffRow", "Staff", Some(models.template.Icons.staffRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastUpdate" -> false)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.store.routes.StaffRowController.view(model.staffId))
          case _ => Ok(views.html.admin.store.staffRowList(app.cfg(u = Some(request.identity), "store", "staff"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(StaffRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("StaffRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(staffId: Int, t: Option[String] = None) = withSession("view", ("store", "StaffRow", "view")) { implicit request => implicit td =>
    val creds: Credentials = request
    val modelF = svc.getByPrimaryKeyRequired(creds, staffId)
    val notesF = noteSvc.getFor(creds, "StaffRow", staffId)
    val storeIdF = modelF.flatMap(m => storeRowS.getByPrimaryKey(creds, m.storeId))
    val addressIdF = modelF.flatMap(m => addressRowS.getByPrimaryKey(creds, m.addressId))

    storeIdF.flatMap(storeIdR => addressIdF.flatMap(addressIdR =>
      notesF.flatMap(notes => modelF.map { model =>
        renderChoice(t) {
          case MimeTypes.HTML => Ok(views.html.admin.store.staffRowView(app.cfg(u = Some(request.identity), "store", "staff", model.staffId.toString), model, notes, storeIdR, addressIdR, app.config.debug))
          case MimeTypes.JSON => Ok(model.asJson)
        }
      })))
  }

  def editForm(staffId: Int) = withSession("edit.form", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.store.routes.StaffRowController.view(staffId)
    val call = controllers.admin.store.routes.StaffRowController.edit(staffId)
    svc.getByPrimaryKey(request, staffId).map {
      case Some(model) => Ok(
        views.html.admin.store.staffRowForm(app.cfg(Some(request.identity), "store", "staff", "Edit"), model, s"Staff [$staffId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No StaffRow found with staffId [$staffId]")
    }
  }

  def edit(staffId: Int) = withSession("edit", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    svc.update(request, staffId = staffId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.store.routes.StaffRowController.view(res._1.staffId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(staffId: Int) = withSession("remove", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, staffId = staffId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.store.routes.StaffRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.store.routes.StaffRowController.list()
    val call = controllers.admin.store.routes.StaffRowController.create()
    Future.successful(Ok(views.html.admin.store.staffRowForm(
      app.cfg(u = Some(request.identity), "store", "staff", "Create"), StaffRow.empty(), "New Staff", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.store.routes.StaffRowController.view(model.staffId))
      case None => Redirect(controllers.admin.store.routes.StaffRowController.list())
    }
  }

  def bulkEditForm = withSession("bulk.edit.form", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    val act = controllers.admin.store.routes.StaffRowController.bulkEdit()
    Future.successful(Ok(views.html.admin.store.staffRowBulkForm(app.cfg(Some(request.identity), "store", "staff", "Bulk Edit"), Nil, act, debug = app.config.debug)))
  }
  def bulkEdit = withSession("bulk.edit", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val typed = pks.map(pk => pk.head.toInt)
    val changes = modelForm(request.body)
    svc.updateBulk(request, typed, changes).map(msg => Ok("OK: " + msg))
  }

  def byStoreId(storeId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.storeId", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByStoreId(request, storeId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "staff", "Store Id")
          val list = views.html.admin.store.staffRowByStoreId(cfg, storeId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Staff by Store Id [$storeId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("StaffRow by storeId", svc.csvFor(0, models))
      })
    }
  }

  def byStoreIdBulkForm(storeId: Int) = {
    withSession("get.by.storeId", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
      svc.getByStoreId(request, storeId).map { modelSeq =>
        val act = controllers.admin.store.routes.StaffRowController.bulkEdit()
        Ok(views.html.admin.store.staffRowBulkForm(app.cfg(Some(request.identity), "store", "staff", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }

  def byAddressId(addressId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.addressId", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByAddressId(request, addressId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "staff", "Address Id")
          val list = views.html.admin.store.staffRowByAddressId(cfg, addressId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Staff by Address Id [$addressId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("StaffRow by addressId", svc.csvFor(0, models))
      })
    }
  }

  def byAddressIdBulkForm(addressId: Int) = {
    withSession("get.by.addressId", ("store", "StaffRow", "edit")) { implicit request => implicit td =>
      svc.getByAddressId(request, addressId).map { modelSeq =>
        val act = controllers.admin.store.routes.StaffRowController.bulkEdit()
        Ok(views.html.admin.store.staffRowBulkForm(app.cfg(Some(request.identity), "store", "staff", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
  def relationCounts(staffId: Int) = withSession("relation.counts", ("store", "StaffRow", "view")) { implicit request => implicit td =>
    val paymentRowByStaffIdF = paymentRowS.countByStaffId(request, staffId)
    val rentalRowByStaffIdF = rentalRowS.countByStaffId(request, staffId)
    for (paymentRowByStaffIdC <- paymentRowByStaffIdF; rentalRowByStaffIdC <- rentalRowByStaffIdF) yield {
      Ok(Seq(
        RelationCount(model = "paymentRow", field = "staffId", count = paymentRowByStaffIdC),
        RelationCount(model = "rentalRow", field = "staffId", count = rentalRowByStaffIdC)
      ).asJson)
    }
  }
}
