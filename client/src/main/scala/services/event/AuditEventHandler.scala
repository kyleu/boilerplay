package services.event

import models._
import services.audit.AuditActivity

class AuditEventHandler() extends EventHandler {
  override def onResponseMessage(msg: ResponseMessage) = msg match {
    case asn: AuditStartNotification => AuditActivity.onStart(asn)
    case acn: AuditCompleteNotification => AuditActivity.onComplete(acn)
    case an: AuditNotification => AuditActivity.onAudit(an)

    case _ => super.onResponseMessage(msg)
  }
}
