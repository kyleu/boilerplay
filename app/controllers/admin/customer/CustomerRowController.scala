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
import models.customer.{CustomerRow, CustomerRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.customer.{CustomerRowService, RentalRowService}
import services.payment.PaymentRowService

@javax.inject.Singleton
class CustomerRowController @javax.inject.Inject() (
    override val app: Application, svc: CustomerRowService, noteSvc: NoteService,
    paymentRowS: PaymentRowService, rentalRowS: RentalRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("customer", "CustomerRow", "Customer", Some(models.template.Icons.customerRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastUpdate" -> false)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("customer", "CustomerRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.customer.routes.CustomerRowController.view(model.customerId))
          case _ => Ok(views.html.admin.customer.customerRowList(app.cfg(u = Some(request.identity), "customer", "customer"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(CustomerRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("CustomerRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("customer", "CustomerRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(customerId: Int, t: Option[String] = None) = withSession("view", ("customer", "CustomerRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, customerId)
    val notesF = noteSvc.getFor(request, "CustomerRow", customerId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.customer.customerRowView(app.cfg(u = Some(request.identity), "customer", "customer", model.customerId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
      }
      case None => NotFound(s"No CustomerRow found with customerId [$customerId]")
    })
  }

  def editForm(customerId: Int) = withSession("edit.form", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.customer.routes.CustomerRowController.view(customerId)
    val call = controllers.admin.customer.routes.CustomerRowController.edit(customerId)
    svc.getByPrimaryKey(request, customerId).map {
      case Some(model) => Ok(
        views.html.admin.customer.customerRowForm(app.cfg(Some(request.identity), "customer", "customer", "Edit"), model, s"Customer [$customerId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No CustomerRow found with customerId [$customerId]")
    }
  }

  def edit(customerId: Int) = withSession("edit", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
    svc.update(request, customerId = customerId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.customer.routes.CustomerRowController.view(res._1.customerId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(customerId: Int) = withSession("remove", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, customerId = customerId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.customer.routes.CustomerRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.customer.routes.CustomerRowController.list()
    val call = controllers.admin.customer.routes.CustomerRowController.create()
    Future.successful(Ok(views.html.admin.customer.customerRowForm(
      app.cfg(u = Some(request.identity), "customer", "customer", "Create"), CustomerRow.empty(), "New Customer", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.customer.routes.CustomerRowController.view(model.customerId))
      case None => Redirect(controllers.admin.customer.routes.CustomerRowController.list())
    }
  }

  def bulkEdit = withSession("bulk.edit", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val changes = modelForm(request.body)
    svc.updateBulk(request, pks, changes).map(msg => Ok("OK: " + msg))
  }

  def byStoreId(storeId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.storeId", ("customer", "CustomerRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByStoreId(request, storeId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "customer", "customer", "Store Id")
          val list = views.html.admin.customer.customerRowByStoreId(cfg, storeId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Customers by Store Id [$storeId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("CustomerRow by storeId", svc.csvFor(0, models))
      })
    }
  }

  def byStoreIdBulkForm(storeId: Int) = {
    withSession("get.by.storeId", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
      svc.getByStoreId(request, storeId).map { modelSeq =>
        val act = controllers.admin.customer.routes.CustomerRowController.bulkEdit()
        Ok(views.html.admin.customer.customerRowBulkForm(app.cfg(Some(request.identity), "customer", "customer", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }

  def byAddressId(addressId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.addressId", ("customer", "CustomerRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByAddressId(request, addressId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "customer", "customer", "Address Id")
          val list = views.html.admin.customer.customerRowByAddressId(cfg, addressId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Customers by Address Id [$addressId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("CustomerRow by addressId", svc.csvFor(0, models))
      })
    }
  }

  def byAddressIdBulkForm(addressId: Int) = {
    withSession("get.by.addressId", ("customer", "CustomerRow", "edit")) { implicit request => implicit td =>
      svc.getByAddressId(request, addressId).map { modelSeq =>
        val act = controllers.admin.customer.routes.CustomerRowController.bulkEdit()
        Ok(views.html.admin.customer.customerRowBulkForm(app.cfg(Some(request.identity), "customer", "customer", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
  def relationCounts(customerId: Int) = withSession("relation.counts", ("customer", "CustomerRow", "view")) { implicit request => implicit td =>
    val paymentRowByCustomerIdF = paymentRowS.countByCustomerId(request, customerId)
    val rentalRowByCustomerIdF = rentalRowS.countByCustomerId(request, customerId)
    for (paymentRowByCustomerIdC <- paymentRowByCustomerIdF; rentalRowByCustomerIdC <- rentalRowByCustomerIdF) yield {
      Ok(Seq(
        RelationCount(model = "paymentRow", field = "customerId", count = paymentRowByCustomerIdC),
        RelationCount(model = "rentalRow", field = "customerId", count = rentalRowByCustomerIdC)
      ).asJson)
    }
  }
}
