/* Generated File */
package controllers.admin.customer

import com.kyleu.projectile.controllers.{BaseController, ServiceAuthController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ControllerUtils
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.customer.{RentalRow, RentalRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.RentalRowService
import services.payment.PaymentRowService

@javax.inject.Singleton
class RentalRowController @javax.inject.Inject() (
    override val app: Application, svc: RentalRowService, noteSvc: NoteService,
    paymentRowS: PaymentRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("customer", "RentalRow", "Rental", Some(models.template.Icons.rentalRow), "view", "edit")
  private[this] val defaultOrderBy = None

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.customer.routes.RentalRowController.view(model.rentalId))
          case _ => Ok(views.html.admin.customer.rentalRowList(app.cfg(u = Some(request.identity), "customer", "rental"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(RentalRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("RentalRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(rentalId: Long, t: Option[String] = None) = withSession("view", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, rentalId)
    val notesF = noteSvc.getFor(request, "RentalRow", rentalId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.customer.rentalRowView(app.cfg(u = Some(request.identity), "customer", "rental", model.rentalId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
      }
      case None => NotFound(s"No RentalRow found with rentalId [$rentalId]")
    })
  }

  def editForm(rentalId: Long) = withSession("edit.form", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.customer.routes.RentalRowController.view(rentalId)
    val call = controllers.admin.customer.routes.RentalRowController.edit(rentalId)
    svc.getByPrimaryKey(request, rentalId).map {
      case Some(model) => Ok(
        views.html.admin.customer.rentalRowForm(app.cfg(Some(request.identity), "customer", "rental", "Edit"), model, s"Rental [$rentalId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No RentalRow found with rentalId [$rentalId]")
    }
  }

  def edit(rentalId: Long) = withSession("edit", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
    svc.update(request, rentalId = rentalId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.customer.routes.RentalRowController.view(res._1.rentalId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(rentalId: Long) = withSession("remove", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, rentalId = rentalId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.customer.routes.RentalRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.customer.routes.RentalRowController.list()
    val call = controllers.admin.customer.routes.RentalRowController.create()
    Future.successful(Ok(views.html.admin.customer.rentalRowForm(
      app.cfg(u = Some(request.identity), "customer", "rental", "Create"), RentalRow.empty(), "New Rental", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.customer.routes.RentalRowController.view(model.rentalId))
      case None => Redirect(controllers.admin.customer.routes.RentalRowController.list())
    }
  }

  def bulkEdit = withSession("bulk.edit", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val changes = modelForm(request.body)
    svc.updateBulk(request, pks, changes).map(msg => Ok("OK: " + msg))
  }

  def byStaffId(staffId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.staffId", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByStaffId(request, staffId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "customer", "rental", "Staff Id")
          val list = views.html.admin.customer.rentalRowByStaffId(cfg, staffId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Rentals by Staff Id [$staffId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("RentalRow by staffId", svc.csvFor(0, models))
      })
    }
  }

  def byStaffIdBulkForm(staffId: Int) = {
    withSession("get.by.staffId", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
      svc.getByStaffId(request, staffId).map { modelSeq =>
        val act = controllers.admin.customer.routes.RentalRowController.bulkEdit()
        Ok(views.html.admin.customer.rentalRowBulkForm(app.cfg(Some(request.identity), "customer", "rental", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }

  def byInventoryId(inventoryId: Long, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.inventoryId", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByInventoryId(request, inventoryId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "customer", "rental", "Inventory Id")
          val list = views.html.admin.customer.rentalRowByInventoryId(cfg, inventoryId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Rentals by Inventory Id [$inventoryId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("RentalRow by inventoryId", svc.csvFor(0, models))
      })
    }
  }

  def byInventoryIdBulkForm(inventoryId: Long) = {
    withSession("get.by.inventoryId", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
      svc.getByInventoryId(request, inventoryId).map { modelSeq =>
        val act = controllers.admin.customer.routes.RentalRowController.bulkEdit()
        Ok(views.html.admin.customer.rentalRowBulkForm(app.cfg(Some(request.identity), "customer", "rental", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }

  def byCustomerId(customerId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.customerId", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByCustomerId(request, customerId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "customer", "rental", "Customer Id")
          val list = views.html.admin.customer.rentalRowByCustomerId(cfg, customerId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Rentals by Customer Id [$customerId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("RentalRow by customerId", svc.csvFor(0, models))
      })
    }
  }

  def byCustomerIdBulkForm(customerId: Int) = {
    withSession("get.by.customerId", ("customer", "RentalRow", "edit")) { implicit request => implicit td =>
      svc.getByCustomerId(request, customerId).map { modelSeq =>
        val act = controllers.admin.customer.routes.RentalRowController.bulkEdit()
        Ok(views.html.admin.customer.rentalRowBulkForm(app.cfg(Some(request.identity), "customer", "rental", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
  def relationCounts(rentalId: Long) = withSession("relation.counts", ("customer", "RentalRow", "view")) { implicit request => implicit td =>
    val paymentRowByRentalIdF = paymentRowS.countByRentalId(request, rentalId)
    for (paymentRowByRentalIdC <- paymentRowByRentalIdF) yield {
      Ok(Seq(
        RelationCount(model = "paymentRow", field = "rentalId", count = paymentRowByRentalIdC)
      ).asJson)
    }
  }
}
