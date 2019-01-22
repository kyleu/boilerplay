/* Generated File */
package controllers.admin.settings

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import models.settings.{Setting, SettingKeyType, SettingResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import services.audit.AuditRecordRowService
import services.settings.SettingService

@javax.inject.Singleton
class SettingController @javax.inject.Inject() (
    override val app: Application, svc: SettingService, noteSvc: NoteService
) extends ServiceAuthController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.settings.routes.SettingController.list()
    val call = controllers.admin.settings.routes.SettingController.create()
    Future.successful(Ok(views.html.admin.settings.settingForm(
      request.identity, Setting.empty(), "New Setting", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.settings.routes.SettingController.view(model.k))
      case None => Redirect(controllers.admin.settings.routes.SettingController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.settings.settingList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(SettingResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("Setting", svc.csvFor(r._1, r._2))
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(k: SettingKeyType, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, k)
    val notesF = noteSvc.getFor(request, "setting", k)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.settings.settingView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No Setting found with k [$k].")
    })
  }

  def editForm(k: SettingKeyType) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.settings.routes.SettingController.view(k)
    val call = controllers.admin.settings.routes.SettingController.edit(k)
    svc.getByPrimaryKey(request, k).map {
      case Some(model) => Ok(
        views.html.admin.settings.settingForm(request.identity, model, s"Setting [$k]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No Setting found with k [$k].")
    }
  }

  def edit(k: SettingKeyType) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, k = k, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.settings.routes.SettingController.view(res._1.k)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(k: SettingKeyType) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, k = k).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.settings.routes.SettingController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
