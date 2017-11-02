package services.audit

import models.{AuditCompleteNotification, AuditNotification, AuditStartNotification}
import org.scalajs.jquery.{jQuery => $}

object AuditActivity {
  lazy val ul = $("#activity-log")

  def onStart(asn: AuditStartNotification) = ul.append(AuditStartTemplate.forMessage(asn.id, asn.as).toString())
  def onComplete(acn: AuditCompleteNotification) = ul.append(AuditCompleteTemplate.forMessage(acn.ac).toString())
  def onAudit(an: AuditNotification) = ul.append(AuditTemplate.forMessage(an.a).toString())
}
