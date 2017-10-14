package controllers.admin.audit

import java.util.UUID

import controllers.BaseController
import models.Application
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._
import models.audit._
import models.result.orderBy.OrderBy
import services.audit.{AuditService, AuditRecordService}
import util.DateUtils
import util.web.ControllerUtils.acceptsCsv

import scala.concurrent.Future

@javax.inject.Singleton
class AuditController @javax.inject.Inject() (override val app: Application, svc: AuditService, recordSvc: AuditRecordService) extends BaseController("audit") {
  import app.contexts.webContext

  def index() = withSession("index") { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.audit.auditIndex(request.identity, app.config.debug)))
  }

  def form() = withSession("form") { implicit request => implicit td =>
    val sJson = audit(pk("user", request.identity.id))(request.identity).asJson.spaces2
    Future.successful(Ok(views.html.admin.audit.auditForm(request.identity, sJson, app.config.debug)))
  }

  def start() = withoutSession("start") { implicit request => implicit td =>
    val json = jsonFor(request)
    val auditStart = json.as[AuditStart] match {
      case Left(x) => throw new IllegalStateException(x.toString)
      case Right(x) => x
    }

    val startMs = DateUtils.nowMillis
    val auditId = UUID.randomUUID

    AuditService.onStart(auditId, auditStart).map { ok =>
      val elapsedMs = DateUtils.nowMillis - startMs
      val response = s"""{ "id": "$auditId", "status": "OK", "elapsed": $elapsedMs }"""
      Ok(response).as(JSON)
    }
  }

  def complete() = withoutSession("complete") { implicit request => implicit td =>
    val json = jsonFor(request)
    val auditComplete = json.as[AuditComplete] match {
      case Left(x) => throw new IllegalStateException(x.toString)
      case Right(x) => x
    }

    val startMs = DateUtils.nowMillis

    AuditService.onComplete(auditComplete).map { ok =>
      val elapsedMs = DateUtils.nowMillis - startMs
      val response = s"""{ "id": "${auditComplete.id}", "status": "OK", "elapsed": $elapsedMs }"""
      Ok(response).as(JSON)
    }
  }

  def list(q: Option[String], orderBy: Option[String], orderAsc: Boolean, limit: Option[Int], offset: Option[Int]) = {
    withSession("list", admin = true) { implicit request => implicit td =>
      val startMs = util.DateUtils.nowMillis
      val orderBys = OrderBy.forVals(orderBy, orderAsc).toSeq
      val f = q match {
        case Some(query) if query.nonEmpty => svc.searchWithCount(query, Nil, orderBys, limit.orElse(Some(100)), offset)
        case _ => svc.getAllWithCount(Nil, orderBys, limit.orElse(Some(100)), offset)
      }
      f.map { r =>
        render {
          case Accepts.Html() => Ok(views.html.admin.audit.auditList(
            request.identity, Some(r._1), r._2, q, orderBy, orderAsc, limit.getOrElse(100), offset.getOrElse(0)
          ))
          case Accepts.Json() => Ok(AuditResult.fromRecords(q, Nil, orderBys, limit, offset, startMs, r._1, r._2).asJson.spaces2).as(JSON)
          case acceptsCsv() => Ok(svc.csvFor("Audit", r._1, r._2)).as("text/csv")
        }
      }
    }
  }

  def view(id: UUID) = withSession("view", admin = true) { implicit request => implicit td =>
    svc.getByPrimaryKey(id).flatMap {
      case Some(model) => recordSvc.getByAuditId(id, Nil, None, None).map { records =>
        render {
          case Accepts.Html() => Ok(views.html.admin.audit.auditView(request.identity, model.copy(records = records), app.config.debug))
          case Accepts.Json() => Ok(model.copy(records = records).asJson.spaces2).as(JSON)
        }
      }
      case None => Future.successful(NotFound(s"No Ad found with id [$id]."))
    }
  }

  def remove(id: UUID) = withSession("remove", admin = true) { implicit request => implicit td =>
    svc.remove(id = id).map(_ => render {
      case Accepts.Html() => Redirect(controllers.admin.audit.routes.AuditController.list())
      case Accepts.Json() => Ok("{ \"status\": \"removed\" }").as(JSON)
    })
  }
}
