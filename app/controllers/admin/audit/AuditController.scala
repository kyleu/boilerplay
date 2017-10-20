package controllers.admin.audit

import java.util.UUID

import controllers.BaseController
import models.Application
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._
import models.audit._
import models.result.RelationCount
import models.result.orderBy.OrderBy
import services.audit.{AuditRecordService, AuditService}
import util.web.ControllerUtils.acceptsCsv

import scala.concurrent.Future

@javax.inject.Singleton
class AuditController @javax.inject.Inject() (override val app: Application, svc: AuditService, recordS: AuditRecordService) extends BaseController("audit") {
  import app.contexts.webContext

  def createForm = withSession("create.form", admin = true) { implicit request => implicit td =>
    val cancel = controllers.admin.audit.routes.AuditController.list()
    val call = controllers.admin.audit.routes.AuditController.create()
    Future.successful(Ok(views.html.admin.audit.auditForm(
      request.identity, models.audit.Audit(user = Some(request.identity.id)), "New Audit", cancel, call, isNew = true, debug = app.config.debug
    )))
  }

  def create = withSession("create", admin = true) { implicit request => implicit td =>
    val fields = modelForm(request.body.asFormUrlEncoded)
    svc.create(fields)
    Future.successful(Ok(play.twirl.api.Html(fields.toString)))
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

  def view(id: UUID) = withSession("view", admin = true) { implicit request => implicit td =>
    svc.getByPrimaryKey(id) match {
      case Some(model) =>
        val records = recordS.getByAuditId(id, Nil, None, None)
        Future.successful(render {
          case Accepts.Html() => Ok(views.html.admin.audit.auditView(request.identity, model.copy(records = records), app.config.debug))
          case Accepts.Json() => Ok(model.copy(records = records).asJson.spaces2).as(JSON)
        })
      case None => Future.successful(NotFound(s"No Ad found with id [$id]."))
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
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.view(res.id))
      case Accepts.Json() => Ok(res.asJson.spaces2).as(JSON)
    })
  }

  def remove(id: UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(id = id)
    Future.successful(render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.list())
      case Accepts.Json() => Ok("{ \"status\": \"removed\" }").as(JSON)
    })
  }

  def relationCounts(id: java.util.UUID) = withSession("relation.counts", admin = true) { implicit request => implicit td =>
    val auditRecordByAuditId = recordS.countByAuditId(id)
    Future.successful(Ok(Seq(
      RelationCount(model = "auditRecord", field = "auditId", count = auditRecordByAuditId)
    ).asJson.spaces2).as(JSON))
  }
}
