package controllers.admin.audit

import java.util.UUID

import controllers.BaseController
import io.circe.syntax._
import models.Application
import models.audit.AuditResult
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
    Future.successful(Ok(views.html.admin.audit.auditForm(
      request.identity, models.audit.Audit.empty(userId = Some(request.identity.id)), "New Audit", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    svc.create(modelForm(request.body.asFormUrlEncoded)) match {
      case Some(model) => Future.successful(Redirect(controllers.admin.audit.routes.AuditController.view(model.id)))
      case None => Future.successful(Redirect(controllers.admin.audit.routes.AuditController.list()))
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      val r = q match {
        case Some(query) if query.nonEmpty => svc.searchWithCount(query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => svc.getAllWithCount(Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      Future.successful(render {
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
      val models = svc.getByUserId(userId, orderBys, limit, offset)
      Future.successful(render {
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
      val r = q match {
        case Some(query) if query.nonEmpty => svc.search(query, Nil, orderBys, limit.orElse(Some(5)), None)
        case _ => svc.getAll(Nil, orderBys, limit.orElse(Some(5)))
      }
      Future.successful(Ok(r.map(_.toSummary).asJson.spaces2).as(JSON))
    }
  }

  def view(id: java.util.UUID) = withSession("view", admin = true) { implicit request => implicit td =>
    val notes = app.noteService.getFor("audit", id)
    svc.getByPrimaryKey(id) match {
      case Some(model) => Future.successful(render {
        case Accepts.Html() => Ok(views.html.admin.audit.auditView(request.identity, model, notes, app.config.debug))
        case Accepts.Json() => Ok(model.asJson.spaces2).as(JSON)
      })
      case None => Future.successful(NotFound(s"No Audit found with id [$id]."))
    }
  }

  def editForm(id: java.util.UUID) = withSession("edit.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditController.view(id)
    val call = controllers.admin.audit.routes.AuditController.edit(id)
    svc.getByPrimaryKey(id) match {
      case Some(model) => Future.successful(Ok(
        views.html.admin.audit.auditForm(request.identity, model, s"Audit [$id]", cancel, call, debug = app.config.debug)
      ))
      case None => Future.successful(NotFound(s"No Audit found with id [$id]."))
    }
  }

  def edit(id: java.util.UUID) = withSession("edit", admin = true) { implicit request => implicit td =>
    val fields = modelForm(request.body.asFormUrlEncoded)
    val res = svc.update(id = id, fields = fields)
    Future.successful(render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.view(res._1.id)).flashing("success" -> res._2)
      case Accepts.Json() => Ok(res.asJson.spaces2).as(JSON)
    })
  }

  def remove(id: java.util.UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(id = id)
    Future.successful(render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.list())
      case Accepts.Json() => Ok("{ \"status\": \"removed\" }").as(JSON)
    })
  }

  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val auditRecordByAuditId = auditRecordS.countByAuditId(id)
    Future.successful(Ok(Seq(
      RelationCount(model = "auditRecord", field = "auditId", count = auditRecordByAuditId)
    ).asJson.spaces2).as(JSON))
  }
}
