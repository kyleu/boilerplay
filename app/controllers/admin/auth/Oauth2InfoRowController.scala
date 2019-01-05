/* Generated File */
package controllers.admin.auth

import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.web.util.ReftreeUtils._
import controllers.admin.ServiceController
import models.Application
import models.auth.{Oauth2InfoRow, Oauth2InfoRowResult}
import play.api.http.MimeTypes
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import services.audit.AuditRecordRowService
import services.auth.Oauth2InfoRowService

@javax.inject.Singleton
class Oauth2InfoRowController @javax.inject.Inject() (
    override val app: Application, svc: Oauth2InfoRowService, auditRecordSvc: AuditRecordRowService
) extends ServiceController(svc) {

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.auth.routes.Oauth2InfoRowController.list()
    val call = controllers.admin.auth.routes.Oauth2InfoRowController.create()
    Future.successful(Ok(views.html.admin.auth.oauth2InfoRowForm(
      request.identity, Oauth2InfoRow.empty(), "New OAuth2 Info", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.auth.routes.Oauth2InfoRowController.view(model.provider, model.key))
      case None => Redirect(controllers.admin.auth.routes.Oauth2InfoRowController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.auth.oauth2InfoRowList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(Oauth2InfoRowResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.csv => csvResponse("Oauth2InfoRow", svc.csvFor(r._1, r._2))
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

    modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.auth.oauth2InfoRowView(request.identity, model, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No Oauth2InfoRow found with provider, key [$provider, $key].")
    }
  }

  def editForm(provider: String, key: String) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.auth.routes.Oauth2InfoRowController.view(provider, key)
    val call = controllers.admin.auth.routes.Oauth2InfoRowController.edit(provider, key)
    svc.getByPrimaryKey(request, provider, key).map {
      case Some(model) => Ok(
        views.html.admin.auth.oauth2InfoRowForm(request.identity, model, s"OAuth2 Info [$provider, $key]", cancel, call, debug = app.config.debug)
      )
      case None => NotFound(s"No Oauth2InfoRow found with provider, key [$provider, $key].")
    }
  }

  def edit(provider: String, key: String) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, provider = provider, key = key, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.auth.routes.Oauth2InfoRowController.view(res._1.provider, res._1.key)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(provider: String, key: String) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, provider = provider, key = key).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.auth.routes.Oauth2InfoRowController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }
}
