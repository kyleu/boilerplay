package services.audit

import models.auth.Credentials
import models.result.data.DataFieldModel
import services.ServiceRegistry
import services.audit.AuditArgs._
import util.Logging
import util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class AuditLookup @javax.inject.Inject() (registry: ServiceRegistry) extends Logging {
  def getByPk(creds: Credentials, model: String, pk: String*)(implicit traceData: TraceData) = {
    getModel(creds, model, getArg(pk.toIndexedSeq, _))
  }

  private[this] def getModel(
    creds: Credentials, key: String, arg: Int => String
  )(implicit traceData: TraceData): Future[Option[DataFieldModel]] = key.toLowerCase match {
    /* Start registry lookups */
    case "auditrecord" => registry.auditServices.auditRecordService.getByPrimaryKey(creds, uuidArg(arg(0)))
    case "note" => registry.noteServices.noteService.getByPrimaryKey(creds, uuidArg(arg(0)))
    case "scheduledtaskrun" => registry.taskServices.scheduledTaskRunService.getByPrimaryKey(creds, uuidArg(arg(0)))
    case "syncprogress" => registry.syncServices.syncProgressService.getByPrimaryKey(creds, stringArg(arg(0)))
    case "systemuser" => registry.userServices.systemUserService.getByPrimaryKey(creds, uuidArg(arg(0)))
    /* End registry lookups */
    case _ =>
      log.warn(s"Attempted to load invalid object type [$key:${arg(0)}].")
      Future.successful(None)
  }
}
