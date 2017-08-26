package models.notification

import models.result.data.DataField

case class Notification(act: String, t: String, ids: Seq[DataField], msg: String, changes: Seq[NotificationField])
