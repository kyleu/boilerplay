package controllers.admin

import akka.util.Timeout
import controllers.BaseController
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.scheduled.ScheduledTask

import services.sandbox._
import services.user.AuthenticationEnvironment
import utils.DateUtils

import scala.concurrent.Future
import scala.concurrent.duration._

object SandboxController {
  val sandboxes = Seq(
    "scratchpad" -> "A one-off I don't feel like putting anwhere else.",
    "scheduled-task" -> "Run the scheduled task.",
    "error-mail" -> "Send the error email.",
    "backfill-metrics" -> "Backfill missing daily metrics."
  )
}

@javax.inject.Singleton
class SandboxController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment,
    scheduledTask: ScheduledTask
) extends BaseController {
  implicit val timeout = Timeout(10.seconds)

  def defaultSandbox() = sandbox("list")

  def sandbox(key: String) = withAdminSession(key) { implicit request =>
    val ret = key match {
      case "scratchpad" => Scratchpad.run()
      case "scheduled-task" => runScheduledTask()
      case "error-mail" => runErrorMail()
      case "backfill-metrics" => BackfillMetrics.run()
      case x => throw new IllegalArgumentException(s"Invalid sandbox [$x].")
    }
    ret.map {
      case s: String => Ok(s)
      case other => Ok("?: " + other.getClass.getName)
    }
  }

  private[this] def runScheduledTask() = scheduledTask.go(true).map { ret =>
    ret.map(x => s"${x._1}: ${x._2.getOrElse("No progress")}").mkString("\n")
  }

  private[this] def runErrorMail() = Future.successful(
    views.html.email.severeErrorHtml(
      "Error Message",
      "Test Context",
      Some(new Exception("Text Exception")),
      None,
      DateUtils.now
    )
  )
}
