package services.task.scheduled

import models.auth.UserCredentials
import models.task.scheduled.ScheduledTask
import com.kyleu.projectile.util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class HelloWorldTask @javax.inject.Inject() () extends ScheduledTask(
  key = "helloWorld",
  title = "Hello World",
  description = Some("Greets the entire freaking planet."),
  runFrequencySeconds = 600
) {
  override def run(creds: UserCredentials, log: String => Unit)(implicit td: TraceData) = {
    log("Hello, world!")
    Future.successful(false)
  }
}
