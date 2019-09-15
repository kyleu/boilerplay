/* Generated File */
package controllers.admin.address

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
import models.address.{CityRow, CityRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.address.{AddressRowService, CityRowService}

@javax.inject.Singleton
class CityRowController @javax.inject.Inject() (
    override val app: Application, svc: CityRowService, noteSvc: NoteService,
    addressRowS: AddressRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("address", "CityRow", "City", Some(models.template.Icons.cityRow), "view", "edit")
  private[this] val defaultOrderBy = Some("lastUpdate" -> false)

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("address", "CityRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.address.routes.CityRowController.view(model.cityId))
          case _ => Ok(views.html.admin.address.cityRowList(app.cfg(u = Some(request.identity), "address", "city"), Some(r._1), r._2, q, orderBys.headOption.map(_.col), orderBys.exists(_.dir.asBool), limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(CityRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case BaseController.MimeTypes.csv => csvResponse("CityRow", svc.csvFor(r._1, r._2))
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("address", "CityRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(cityId: Int, t: Option[String] = None) = withSession("view", ("address", "CityRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, cityId)
    val notesF = noteSvc.getFor(request, "CityRow", cityId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.address.cityRowView(app.cfg(u = Some(request.identity), "address", "city", model.cityId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
      }
      case None => NotFound(s"No CityRow found with cityId [$cityId]")
    })
  }

  def editForm(cityId: Int) = withSession("edit.form", ("address", "CityRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.address.routes.CityRowController.view(cityId)
    val call = controllers.admin.address.routes.CityRowController.edit(cityId)
    svc.getByPrimaryKey(request, cityId).map {
      case Some(model) => Ok(
        views.html.admin.address.cityRowForm(app.cfg(Some(request.identity), "address", "city", "Edit"), model, s"City [$cityId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No CityRow found with cityId [$cityId]")
    }
  }

  def edit(cityId: Int) = withSession("edit", ("address", "CityRow", "edit")) { implicit request => implicit td =>
    svc.update(request, cityId = cityId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.address.routes.CityRowController.view(res._1.cityId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(cityId: Int) = withSession("remove", ("address", "CityRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, cityId = cityId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.address.routes.CityRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
  def createForm = withSession("create.form", ("address", "CityRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.address.routes.CityRowController.list()
    val call = controllers.admin.address.routes.CityRowController.create()
    Future.successful(Ok(views.html.admin.address.cityRowForm(
      app.cfg(u = Some(request.identity), "address", "city", "Create"), CityRow.empty(), "New City", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("address", "CityRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.address.routes.CityRowController.view(model.cityId))
      case None => Redirect(controllers.admin.address.routes.CityRowController.list())
    }
  }

  def bulkEdit = withSession("bulk.edit", ("address", "CityRow", "edit")) { implicit request => implicit td =>
    val form = ControllerUtils.getForm(request.body)
    val pks = form("primaryKeys").split("//").map(_.trim).filter(_.nonEmpty).map(_.split("---").map(_.trim).filter(_.nonEmpty).toList).toList
    val changes = modelForm(request.body)
    svc.updateBulk(request, pks, changes).map(msg => Ok("OK: " + msg))
  }

  def byCountryId(countryId: Int, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None, embedded: Boolean = false) = {
    withSession("get.by.countryId", ("address", "CityRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc, defaultOrderBy).toSeq
      svc.getByCountryId(request, countryId, orderBys, limit, offset).map(models => renderChoice(t) {
        case MimeTypes.HTML =>
          val cfg = app.cfg(Some(request.identity), "address", "city", "Country Id")
          val list = views.html.admin.address.cityRowByCountryId(cfg, countryId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0))
          if (embedded) { Ok(list) } else { Ok(page(s"Cities by Country Id [$countryId]", cfg)(card(None)(list))) }
        case MimeTypes.JSON => Ok(models.asJson)
        case BaseController.MimeTypes.csv => csvResponse("CityRow by countryId", svc.csvFor(0, models))
      })
    }
  }

  def byCountryIdBulkForm(countryId: Int) = {
    withSession("get.by.countryId", ("address", "CityRow", "edit")) { implicit request => implicit td =>
      svc.getByCountryId(request, countryId).map { modelSeq =>
        val act = controllers.admin.address.routes.CityRowController.bulkEdit()
        Ok(views.html.admin.address.cityRowBulkForm(app.cfg(Some(request.identity), "address", "city", "Bulk Edit"), modelSeq, act, debug = app.config.debug))
      }
    }
  }
  def relationCounts(cityId: Int) = withSession("relation.counts", ("address", "CityRow", "view")) { implicit request => implicit td =>
    val addressRowByCityIdF = addressRowS.countByCityId(request, cityId)
    for (addressRowByCityIdC <- addressRowByCityIdF) yield {
      Ok(Seq(
        RelationCount(model = "addressRow", field = "cityId", count = addressRowByCityIdC)
      ).asJson)
    }
  }
}
