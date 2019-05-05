package services.audit

import com.google.inject.Injector
import com.kyleu.projectile.models.auth.UserCredentials
import com.kyleu.projectile.models.result.data.DataFieldModel
import com.kyleu.projectile.services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class AuditLookup @javax.inject.Inject() (injector: Injector) extends Logging {
  def getByPk(creds: UserCredentials, model: String, pk: String*)(implicit traceData: TraceData) = {
    getModel(creds, model, getArg(pk.toIndexedSeq, _))
  }

  private[this] def getModel(
    creds: UserCredentials, key: String, arg: Int => String
  )(implicit traceData: TraceData): Future[Option[DataFieldModel]] = key.toLowerCase match {
    /* Start registry lookups */
    /* Projectile export section [boilerplay] */
    case "scheduledtaskrunrow" => injector.getInstance(classOf[services.task.ScheduledTaskRunRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "syncprogressrow" => injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByPrimaryKey(creds, stringArg(arg(0)))
    /* End registry lookups */
    case _ =>
      log.warn(s"Attempted to load invalid object type [$key:${arg(0)}].")
      Future.successful(None)
  }
}
