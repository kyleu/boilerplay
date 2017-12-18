package controllers.admin.audit

import java.util.UUID

import controllers.BaseController
import io.circe.syntax._
import models.Application
import models.audit.{Audit, AuditResult}
import models.result.RelationCount
import models.result.orderBy.OrderBy

import scala.concurrent.Future
import services.audit.{AuditRecordService, AuditService}
import util.FutureUtils.defaultContext
import util.web.ControllerUtils.acceptsCsv

@javax.inject.Singleton
class AuditController @javax.inject.Inject() (
    override val app: Application, svc: AuditService, auditRecordS: AuditRecordService
) extends BaseController("audit") {
  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditController.list()
    val call = controllers.admin.audit.routes.AuditController.create()
    val audit = Audit(userId = request.identity.id, client = "test", act = "test")
    Future.successful(Ok(views.html.admin.audit.auditForm(
      request.identity, audit, "New Audit", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(request, modelForm(request.body.asFormUrlEncoded)).map {
      case Some(model) => Redirect(controllers.admin.audit.routes.AuditController.view(model.id))
      case None => Redirect(controllers.admin.audit.routes.AuditController.list())
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => svc.searchWithCount(request, query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => svc.getAllWithCount(request, Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      f.map(r => render {
        case Accepts.Html() => Ok(views.html.admin.audit.auditList(
          request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(AuditResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson.spaces2).as(JSON)
        case acceptsCsv() => Ok(svc.csvFor("Audit", r._1, r._2)).as("text/csv")
      })
    }
  }

  def byUserId(userId: UUID, orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("get.by.user.id", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      svc.getByUserId(request, userId, orderBys, limit, offset).map(models => render {
        case Accepts.Html() => Ok(views.html.admin.audit.auditByUserId(
          request.identity, userId, models, orderBy, orderAsc, limit.getOrElse(5), offset.getOrElse(0)
        ))
        case Accepts.Json() => Ok(models.asJson.spaces2).as(JSON)
        case acceptsCsv() => Ok(svc.csvFor("Note by author", 0, models)).as("text/csv")
      })
    }
  }

  def autocomplete(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int]) = {
    withSession("autocomplete", admin = true) { implicit request => implicit td =>
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => svc.search(request, query, Nil, orderBys, limit.orElse(Some(5)), None)
        case _ => svc.getAll(request, Nil, orderBys, limit.orElse(Some(5)))
      }
      f.map(r => Ok(r.map(_.toSummary).asJson.spaces2).as(JSON))
    }
  }

  def view(id: java.util.UUID) = withSession("view", admin = true) { implicit request => implicit td =>
    val modelF = svc.getByPrimaryKey(request, id)
    val notesF = app.coreServices.notes.getFor(request, "audit", id)
    val auditsF = auditRecordS.getByModel(request, "audit", id)

    notesF.flatMap(notes => auditsF.flatMap(audits => modelF.map {
      case Some(model) => render {
        case Accepts.Html() => Ok(views.html.admin.audit.auditView(request.identity, model, notes, audits, app.config.debug))
        case Accepts.Json() => Ok(model.asJson.spaces2).as(JSON)
      }
      case None => NotFound(s"No Audit found with id [$id].")
    }))
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
    val fields = modelForm(request.body.asFormUrlEncoded)
    svc.update(request, id = id, fields = fields).map(res => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson.spaces2).as(JSON)
    })
  }

  def remove(id: java.util.UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(request, id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.list())
      case Accepts.Json() => Ok("{ \"status\": \"removed\" }").as(JSON)
    })
  }

  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    auditRecordS.countByAuditId(request, id).map(auditRecordByAuditId => Ok(Seq(
      RelationCount(model = "auditRecord", field = "auditId", count = auditRecordByAuditId)
    ).asJson.spaces2).as(JSON))
  }
}
