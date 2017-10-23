package services.audit

import models.result.data.DataFieldModel
import models.user.User
import services.ServiceRegistry
import services.audit.AuditArgs._
import util.Logging
import util.tracing.TraceData

@javax.inject.Singleton
class AuditLookup @javax.inject.Inject() (registry: ServiceRegistry) extends Logging {
  def getByPk(user: User, model: String, pk: String*)(implicit traceData: TraceData) = getModel(user, model, getArg(pk, _))

  private[this] def getModel(user: User, key: String, arg: (Int) => String)(implicit traceData: TraceData): Option[DataFieldModel] = key.toLowerCase match {
    /* Start registry lookups */

    case "auditrecord" => registry.auditServices.auditRecordService.getByPrimaryKey(user, uuidArg(arg(0)))
    case "note" => registry.noteServices.noteService.getByPrimaryKey(user, uuidArg(arg(0)))
    case "user" => registry.userServices.userService.getByPrimaryKey(user, uuidArg(arg(0)))

    /* End registry lookups */
    case _ =>
      log.warn(s"Attempted to load invalid object type [$key:${arg(0)}].")
      None
  }
}
