/* Generated File */
package controllers.admin.payment

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ReftreeUtils._
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.views.html.layout.{card, page}
import models.payment.{PaymentRow, PaymentRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.payment.PaymentRowService

@javax.inject.Singleton
class PaymentRowController @javax.inject.Inject() (
    override val app: Application, svc: PaymentRowService, noteSvc: NoteService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("payment", "PaymentRow", "Payment", Some(models.template.Icons.paymentRow), "view", "edit")

  def createForm = withSession("create.form", ("payment", "PaymentRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.payment.routes.PaymentRowController.list()
    val call = controllers.admin.payment.routes.PaymentRowController.create()
    Future.successful(Ok(views.html.admin.payment.paymentRowForm(
      app.cfg(u = Some(request.identity), "payment", "payment", "Create"), PaymentRow.empty(), "New Payment", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("payment", "PaymentRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.payment.routes.PaymentRowController.view(model.paymentId))
      case None => Redirect(controllers.admin.payment.routes.PaymentRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("payment", "PaymentRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.payment.routes.PaymentRowController.view(model.paymentId))
          case _ => Ok(views.html.admin.payment.paymentRowList(app.cfg(u = Some(request.identity), "payment", "payment"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(PaymentRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("PaymentRow", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("payment", "PaymentRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def byStaffId(staffId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.staffId", ("payment", "PaymentRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByStaffId(request, staffId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "payment", "payment", "Staff Id")
          val list = views.html.admin.payment.paymentRowByStaffId(cfg, staffId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Payments by Staff Id [$staffId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("PaymentRow by staffId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byRentalId(rentalId: Long, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.rentalId", ("payment", "PaymentRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByRentalId(request, rentalId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "payment", "payment", "Rental Id")
          val list = views.html.admin.payment.paymentRowByRentalId(cfg, rentalId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Payments by Rental Id [$rentalId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("PaymentRow by rentalId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byCustomerId(customerId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.customerId", ("payment", "PaymentRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByCustomerId(request, customerId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "payment", "payment", "Customer Id")
          val list = views.html.admin.payment.paymentRowByCustomerId(cfg, customerId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Payments by Customer Id [$customerId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.csv => csvResponse("PaymentRow by customerId", svc.csvFor(0, models))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def view(paymentId: Long, t: Option[String] = None) = withSession("view", ("payment", "PaymentRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, paymentId)
    val notesF = noteSvc.getFor(request, "PaymentRow", paymentId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.payment.paymentRowView(app.cfg(u = Some(request.identity), "payment", "payment", model.paymentId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No PaymentRow found with paymentId [$paymentId]")
    })
  }

  def editForm(paymentId: Long) = withSession("edit.form", ("payment", "PaymentRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.payment.routes.PaymentRowController.view(paymentId)
    val call = controllers.admin.payment.routes.PaymentRowController.edit(paymentId)
    svc.getByPrimaryKey(request, paymentId).map {
      case Some(model) => Ok(
        views.html.admin.payment.paymentRowForm(app.cfg(Some(request.identity), "payment", "payment", "Edit"), model, s"Payment [$paymentId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No PaymentRow found with paymentId [$paymentId]")
    }
  }

  def edit(paymentId: Long) = withSession("edit", ("payment", "PaymentRow", "edit")) { implicit request => implicit td =>
    svc.update(request, paymentId = paymentId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.payment.routes.PaymentRowController.view(res._1.paymentId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(paymentId: Long) = withSession("remove", ("payment", "PaymentRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, paymentId = paymentId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.payment.routes.PaymentRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
