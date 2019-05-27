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
import models.store.{StaffRow, StaffRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.RentalRowService
import services.payment.PaymentRowService
import services.store.StaffRowService

@javax.inject.Singleton
class StaffRowController @javax.inject.Inject() (
    override val app: Application, svc: StaffRowService, noteSvc: NoteService,
    rentalRowS: RentalRowService, paymentRowS: PaymentRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("store", "StaffRow", "Staff", Some(models.template.Icons.staffRow), "view", "edit")

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

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.store.routes.StaffRowController.view(model.staffId))
          case _ => Ok(views.html.admin.store.staffRowList(app.cfg(u = Some(request.identity), "store", "staff"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(StaffRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("StaffRow", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def byStoreId(storeId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.storeId", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByStoreId(request, storeId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "staff", "Store Id")
          val list = views.html.admin.store.staffRowByStoreId(cfg, storeId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Staff by Store Id [$storeId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("StaffRow by storeId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byAddressId(addressId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.addressId", ("store", "StaffRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByAddressId(request, addressId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "store", "staff", "Address Id")
          val list = views.html.admin.store.staffRowByAddressId(cfg, addressId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Staff by Address Id [$addressId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("StaffRow by addressId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(staffId: Int, t: Option[String] = None) = withSession("view", ("store", "StaffRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, staffId)
    val notesF = noteSvc.getFor(request, "StaffRow", staffId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.store.staffRowView(app.cfg(u = Some(request.identity), "store", "staff", model.staffId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No StaffRow found with staffId [$staffId]")
    })
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
