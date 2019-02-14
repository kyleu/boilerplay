/* Generated File */
package controllers.admin.auth

import com.kyleu.projectile.controllers.{ServiceAuthController, ServiceController}
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.note.NoteService
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import models.auth.{PasswordInfoRow, PasswordInfoRowResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import services.auth.PasswordInfoRowService

@javax.inject.Singleton
class PasswordInfoRowController @javax.inject.Inject() (
    override val app: Application, authActions: AuthActions, svc: PasswordInfoRowService, noteSvc: NoteService
) extends ServiceAuthController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.auth.routes.PasswordInfoRowController.list()
    val call = controllers.admin.auth.routes.PasswordInfoRowController.create()
    Future.successful(Ok(views.html.admin.auth.passwordInfoRowForm(
      request.identity, authActions, PasswordInfoRow.empty(), "New Password Info", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.auth.routes.PasswordInfoRowController.view(model.provider, model.key))
      case None => Redirect(controllers.admin.auth.routes.PasswordInfoRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.auth.passwordInfoRowList(
          request.identity, authActions, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(PasswordInfoRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("PasswordInfoRow", svc.csvFor(r._1, r._2))
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

  def view(provider: String, key: String, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, provider, key)
    val notesF = noteSvc.getFor(request, "passwordInfoRow", provider, key)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.auth.passwordInfoRowView(request.identity, authActions, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No PasswordInfoRow found with provider, key [$provider, $key]")
    })
  }

  def editForm(provider: String, key: String) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.auth.routes.PasswordInfoRowController.view(provider, key)
    val call = controllers.admin.auth.routes.PasswordInfoRowController.edit(provider, key)
    svc.getByPrimaryKey(request, provider, key).map {
      case Some(model) => Ok(
        views.html.admin.auth.passwordInfoRowForm(request.identity, authActions, model, s"Password Info [$provider, $key]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No PasswordInfoRow found with provider, key [$provider, $key]")
    }
  }

  def edit(provider: String, key: String) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, provider = provider, key = key, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.auth.routes.PasswordInfoRowController.view(res._1.provider, res._1.key)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(provider: String, key: String) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, provider = provider, key = key).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.auth.routes.PasswordInfoRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
