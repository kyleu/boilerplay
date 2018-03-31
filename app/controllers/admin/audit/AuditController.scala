package controllers.admin.audit

import java.util.UUID

import controllers.admin.ServiceController
import models.Application
import models.audit.{Audit, AuditResult}
import models.result.RelationCount
import models.result.orderBy.OrderBy
import play.api.http.MimeTypes

import scala.concurrent.Future
import services.audit.{AuditRecordService, AuditService}
import util.FutureUtils.defaultContext
import util.ReftreeUtils._
import util.JsonSerializers._

@javax.inject.Singleton
class AuditController @javax.inject.Inject() (override val app: Application, svc: AuditService, recordS: AuditRecordService) extends ServiceController(svc) {
  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditController.list()
    val call = controllers.admin.audit.routes.AuditController.create()
    val audit = Audit(userId = request.identity.id, client = "test", act = "test")
    Future.successful(Ok(views.html.admin.audit.auditForm(
      request.identity, audit, "New Audit", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body)).map {
      case Some(model) => Redirect(controllers.admin.audit.routes.AuditController.view(model.id))
      case None => Redirect(controllers.admin.audit.routes.AuditController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      searchWithCount(q, orderBys, limit, offset).map(r => renderChoice(t) {
        case ServiceController.MimeTypes.csv => Ok(svc.csvFor("Audit", r._1, r._2)).as("text/csv")
        case MimeTypes.HTML => Ok(views.html.admin.audit.auditList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(AuditResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = r._2)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = r._2)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def byUserId(userId: UUID, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int], t: Option[String] = None) = {
    withSession("get.by.user.id", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByUserId(request, userId, orderBys, limit, offset).map(models => renderChoice(t) {
        case ServiceController.MimeTypes.csv => Ok(svc.csvFor("Audit by userId", 0, models)).as("text/csv")
        case MimeTypes.HTML => Ok(views.html.admin.audit.auditByUserId(
          request.identity, userId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0)
        ))
        case MimeTypes.JSON => Ok(models.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = models)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = models)).as(ServiceController.MimeTypes.svg)
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      search(q, orderBys, limit, None).map(r => Ok(r.map(_.toSummary).asJson))
    }
  }

  def view(id: java.util.UUID, t: Option[String] = None) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = app.coreServices.notes.getFor(request, "audit", id)

    notesF.flatMap(notes => modelF.map {
      case Some(model) => renderChoice(t) {
        case MimeTypes.HTML => Ok(views.html.admin.audit.auditView(request.identity, model, notes, app.config.debug))
        case MimeTypes.JSON => Ok(model.asJson)
        case ServiceController.MimeTypes.png => Ok(renderToPng(v = model)).as(ServiceController.MimeTypes.png)
        case ServiceController.MimeTypes.svg => Ok(renderToSvg(v = model)).as(ServiceController.MimeTypes.svg)
      }
      case None => NotFound(s"No Audit found with id [$id].")
    })
  }

  def editForm(id: java.util.UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditController.view(id)
    val call = controllers.admin.audit.routes.AuditController.edit(id)
    svc.getByPrimaryKey(request, id).map {
      case Some(model) => Ok(views.html.admin.audit.auditForm(request.identity, model, s"Audit [$id]", cancel, call, debug = app.config.debug))
      case None => NotFound(s"No Audit found with id [$id].")
    }
  }

  def edit(id: java.util.UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    svc.update(request, id = id, fields = modelForm(request.body)).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson)
    })
  }

  def remove(id: java.util.UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.list())
      case Accepts.Json() => Ok(io.circe.Json.obj("status" -> io.circe.Json.fromString("removed")))
    })
  }

  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    recordS.countByAuditId(request, id).map(auditRecordByAuditId => Ok(Seq(
      RelationCount(model = "auditRecord", field = "auditId", count = auditRecordByAuditId)
    ).asJson))
  }
}
