/* Generated File */
package controllers.admin.address

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
import models.address.{AddressRow, AddressRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.address.{AddressRowService, CityRowService}
import services.customer.CustomerRowService
import services.store.{StaffRowService, StoreRowService}

@javax.inject.Singleton
class AddressRowController @javax.inject.Inject() (
    override val app: Application, svc: AddressRowService, noteSvc: NoteService,
    customerRowS: CustomerRowService, staffRowS: StaffRowService, storeRowS: StoreRowService, cityRowS: CityRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("address", "AddressRow", "Address", Some(models.template.Icons.addressRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastUpdate" -> false)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", ("address", "AddressRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.address.routes.AddressRowController.view(model.addressId))
          case _ => Ok(views.html.admin.address.addressRowList(app.cfg(u = Some(request.identity), "address", "address"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(AddressRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("AddressRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("address", "AddressRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(addressId: Int, t: Option[String] = None) = withSession("view", ("address", "AddressRow", "view")) { implicit request => implicit td =>
    val creds: Credentials = request
    val modelF = svc.getByPrimaryKeyRequired(creds, addressId)
    val notesF = noteSvc.getFor(creds, "AddressRow", addressId)
    val cityIdF = modelF.flatMap(m => cityRowS.getByPrimaryKey(creds, m.cityId))

    cityIdF.flatMap(cityIdR =>
      notesF.flatMap(notes => modelF.map { model =>
        renderChoice(t) {
          case MimeTypes.HTML => Ok(views.html.admin.address.addressRowView(app.cfg(u = Some(request.identity), "address", "address", model.addressId.toString), model, notes, cityIdR, app.config.debug))
          case MimeTypes.JSON => Ok(model.asJson)
        }
      }))
  }

  def editForm(addressId: Int) = withSession("edit.form", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.address.routes.AddressRowController.view(addressId)
    val call = controllers.admin.address.routes.AddressRowController.edit(addressId)
    svc.getByPrimaryKey(request, addressId).map {
      case Some(model) => Ok(
        views.html.admin.address.addressRowForm(app.cfg(Some(request.identity), "address", "address", "Edit"), model, s"Address [$addressId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No AddressRow found with addressId [$addressId]")
    }
  }

  def edit(addressId: Int) = withSession("edit", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    svc.update(request, addressId = addressId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.address.routes.AddressRowController.view(res._1.addressId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(addressId: Int) = withSession("remove", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, addressId = addressId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.address.routes.AddressRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.address.routes.AddressRowController.list()
    val call = controllers.admin.address.routes.AddressRowController.create()
    Future.successful(Ok(views.html.admin.address.addressRowForm(
      app.cfg(u = Some(request.identity), "address", "address", "Create"), AddressRow.empty(), "New Address", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.address.routes.AddressRowController.view(model.addressId))
      case None => Redirect(controllers.admin.address.routes.AddressRowController.list())
    }
  }

  def bulkEditForm = withSession("bulk.edit.form", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    val act = controllers.admin.address.routes.AddressRowController.bulkEdit()
    Future.successful(Ok(views.html.admin.address.addressRowBulkForm(app.cfg(Some(request.identity), "address", "address", "Bulk Edit"), Nil, act, debug = app.config.debug)))
  }
  def bulkEdit = withSession("bulk.edit", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val typed = pks.map(pk => pk.head.toInt)
    val changes = modelForm(request.body)
    svc.updateBulk(request, typed, changes).map(msg => Ok("OK: " + msg))
  }

  def byCityId(cityId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.cityId", ("address", "AddressRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByCityId(request, cityId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "address", "address", "City Id")
          val list = views.html.admin.address.addressRowByCityId(cfg, cityId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Addresses by City Id [$cityId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("AddressRow by cityId", svc.csvFor(0, models))
      })
    }
  }

  def byCityIdBulkForm(cityId: Int) = {
    withSession("get.by.cityId", ("address", "AddressRow", "edit")) { implicit request => implicit td =>
      svc.getByCityId(request, cityId).map { modelSeq =>
        val act = controllers.admin.address.routes.AddressRowController.bulkEdit()
        Ok(views.html.admin.address.addressRowBulkForm(app.cfg(Some(request.identity), "address", "address", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
  def relationCounts(addressId: Int) = withSession("relation.counts", ("address", "AddressRow", "view")) { implicit request => implicit td =>
    val customerRowByAddressIdF = customerRowS.countByAddressId(request, addressId)
    val staffRowByAddressIdF = staffRowS.countByAddressId(request, addressId)
    val storeRowByAddressIdF = storeRowS.countByAddressId(request, addressId)
    for (customerRowByAddressIdC <- customerRowByAddressIdF; staffRowByAddressIdC <- staffRowByAddressIdF; storeRowByAddressIdC <- storeRowByAddressIdF) yield {
      Ok(Seq(
        RelationCount(model = "customerRow", field = "addressId", count = customerRowByAddressIdC),
        RelationCount(model = "staffRow", field = "addressId", count = staffRowByAddressIdC),
        RelationCount(model = "storeRow", field = "addressId", count = storeRowByAddressIdC)
      ).asJson)
    }
  }
}
