package models.task.scheduled

import models.auth.Credentials
import util.tracing.TraceData

import scala.concurrent.Future

abstract class ScheduledTask(val key: String, val title: String, val description: Option[String], val runFrequencySeconds: Int) {
  def run(creds: Credentials, log: String => Unit)(implicit td: TraceData): Future[Boolean]
}
