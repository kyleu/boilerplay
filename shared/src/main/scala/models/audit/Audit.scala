package models.audit

import models.result.data.DataField

case class Audit(act: String, t: String, ids: Seq[DataField], msg: String, changes: Seq[AuditField])
