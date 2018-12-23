package services.sync

import models.auth.Credentials
import models.sync.SyncProgressRow
import util.FutureUtils.serviceContext
import util.tracing.TraceData

import scala.util.control.NonFatal

@javax.inject.Singleton
class SyncService @javax.inject.Inject() (val progressSvc: SyncProgressRowService) {
  def set(creds: Credentials, key: String, status: String, msg: String)(implicit td: TraceData) = {
    val progress = SyncProgressRow(key = key, status = status, message = msg, lastTime = util.DateUtils.now).toDataFields
    progressSvc.getByPrimaryKey(creds, key).flatMap {
      case Some(_) => progressSvc.update(creds, key, progress.tail).map(x => Some(x._1))
      case None => progressSvc.create(creds, progress).recover {
        case NonFatal(_) => None
      }
    }
  }
}
