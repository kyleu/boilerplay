package services.audit

import models._
import models.audit.{AuditCompleteTemplate, AuditStartTemplate, AuditTemplate}
import services.event.EventHandler
import org.scalajs.jquery.{jQuery => $}

class AuditEventHandler() extends EventHandler {
  private[this] lazy val ul = $("#activity-log")

  private[this] def onStart(asn: AuditStartNotification) = ul.append(AuditStartTemplate.forMessage(asn.id, asn.as).toString())
  private[this] def onComplete(acn: AuditCompleteNotification) = ul.append(AuditCompleteTemplate.forMessage(acn.ac).toString())
  private[this] def onAudit(an: AuditNotification) = ul.append(AuditTemplate.forMessage(an.a).toString())

  override def onResponseMessage(msg: ResponseMessage) = msg match {
    case asn: AuditStartNotification => onStart(asn)
    case acn: AuditCompleteNotification => onComplete(acn)
    case an: AuditNotification => onAudit(an)

    case _ => super.onResponseMessage(msg)
  }
}
