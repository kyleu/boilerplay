package models.task.scheduled

import com.kyleu.projectile.models.auth.UserCredentials
import com.kyleu.projectile.util.tracing.TraceData

import scala.concurrent.Future

abstract class ScheduledTask(val key: String, val title: String, val description: Option[String], val runFrequencySeconds: Int) {
  def run(creds: UserCredentials, log: String => Unit)(implicit td: TraceData): Future[Boolean]
}
