package controllers.admin.audit

import java.util.UUID

import controllers.{BaseController, ControllerUtilities}
import io.circe.syntax._
import models.Application
import models.audit._
import services.audit.{AuditHelper, AuditService}
import util.DateUtils

import scala.concurrent.Future

@javax.inject.Singleton
class AuditLifecycleController @javax.inject.Inject() (override val app: Application, svc: AuditService) extends BaseController("audit") {
  import app.contexts.webContext

  def index() = withSession("index") { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.audit.auditIndex(request.identity, app.config.debug)))
  }

  def testForm() = withSession("form") { implicit request => implicit td =>
    val sJson = AuditHelper.auditStart(AuditHelper.pk("user", request.identity.id))(request).asJson.spaces2
    Future.successful(Ok(views.html.admin.audit.testForm(request.identity, sJson, app.config.debug)))
  }

  def start() = withSession("start") { implicit request => implicit td =>
    val json = ControllerUtilities.jsonFor(request)
    val auditStart = json.as[AuditStart] match {
      case Left(x) => throw new IllegalStateException(x.toString)
      case Right(x) => x
    }

    val startMs = DateUtils.nowMillis
    val auditId = UUID.randomUUID

    AuditHelper.onStart(request, auditId, auditStart)
    val elapsedMs = DateUtils.nowMillis - startMs
    val response = s"""{ "id": "$auditId", "status": "OK", "elapsed": $elapsedMs }"""
    Future.successful(Ok(response).as(JSON))
  }

  def complete() = withSession("complete") { implicit request => implicit td =>
    val json = ControllerUtilities.jsonFor(request)
    val auditComplete = json.as[AuditComplete] match {
      case Left(x) => throw new IllegalStateException(x.toString)
      case Right(x) => x
    }

    val startMs = DateUtils.nowMillis

    AuditHelper.onComplete(request, auditComplete)
    val elapsedMs = DateUtils.nowMillis - startMs
    val response = s"""{ "id": "${auditComplete.id}", "status": "OK", "elapsed": $elapsedMs }"""
    Future.successful(Ok(response).as(JSON))
  }
}
