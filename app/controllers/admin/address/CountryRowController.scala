/* Generated File */
package controllers.admin.address

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.models.result.RelationCount
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.models.web.ReftreeUtils._
import com.kyleu.projectile.services.auth.PermissionService
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import models.address.{CountryRow, CountryRowResult}
import play.api.http.MimeTypes
import scala.concurrent.{ExecutionContext, Future}
import services.address.{CityRowService, CountryRowService}

@javax.inject.Singleton
class CountryRowController @javax.inject.Inject() (
    override val app: Application, svc: CountryRowService, noteSvc: NoteService,
    cityRowS: CityRowService
)(implicit ec: ExecutionContext) extends ServiceAuthController(svc) {
  PermissionService.registerModel("address", "CountryRow", "Country", Some(models.template.Icons.countryRow), "view", "edit")

  def createForm = withSession("create.form", ("address", "CountryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.address.routes.CountryRowController.list()
    val call = controllers.admin.address.routes.CountryRowController.create()
    Future.successful(Ok(views.html.admin.address.countryRowForm(
      app.cfg(u = Some(request.identity), "address", "country", "Create"), CountryRow.empty(), "New Country", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", ("address", "CountryRow", "edit")) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.address.routes.CountryRowController.view(model.countryId))
      case None => Redirect(controllers.admin.address.routes.CountryRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("view", ("address", "CountryRow", "view")) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => r._2.toList match {
          case model :: Nil if q.nonEmpty => Redirect(controllers.admin.address.routes.CountryRowController.view(model.countryId))
          case _ => Ok(views.html.admin.address.countryRowList(app.cfg(u = Some(request.identity), "address", "country"), Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)))
        }
        case MimeTypes.JSON => Ok(CountryRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("CountryRow", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", ("address", "CountryRow", "view")) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(countryId: Int, t: Option[String] = None) = withSession("view", ("address", "CountryRow", "view")) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, countryId)
    val notesF = noteSvc.getFor(request, "CountryRow", countryId)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.address.countryRowView(app.cfg(u = Some(request.identity), "address", "country", model.countryId.toString), model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No CountryRow found with countryId [$countryId]")
    })
  }

  def editForm(countryId: Int) = withSession("edit.form", ("address", "CountryRow", "edit")) { implicit request => implicit td =>
    val cancel = controllers.admin.address.routes.CountryRowController.view(countryId)
    val call = controllers.admin.address.routes.CountryRowController.edit(countryId)
    svc.getByPrimaryKey(request, countryId).map {
      case Some(model) => Ok(
        views.html.admin.address.countryRowForm(app.cfg(Some(request.identity), "address", "country", "Edit"), model, s"Country [$countryId]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No CountryRow found with countryId [$countryId]")
    }
  }

  def edit(countryId: Int) = withSession("edit", ("address", "CountryRow", "edit")) { implicit request => implicit td =>
    svc.update(request, countryId = countryId, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.address.routes.CountryRowController.view(res._1.countryId))
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(countryId: Int) = withSession("remove", ("address", "CountryRow", "edit")) { implicit request => implicit td =>
    svc.remove(request, countryId = countryId).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.address.routes.CountryRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }

  def relationCounts(countryId: Int) = withSession("relation.counts", ("address", "CountryRow", "view")) { implicit request => implicit td =>
    val cityRowByCountryIdF = cityRowS.countByCountryId(request, countryId)
    for (cityRowByCountryIdC <- cityRowByCountryIdF) yield {
      Ok(Seq(
        RelationCount(model = "cityRow", field = "countryId", count = cityRowByCountryIdC)
      ).asJson)
    }
  }
}
