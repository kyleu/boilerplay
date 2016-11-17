package models.sandbox

import enumeratum._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.{Application, Logging}

import scala.concurrent.Future

sealed abstract class SandboxTask(val id: String, val name: String, val description: String) extends EnumEntry with Logging {
  def run(app: Application): Future[SandboxTask.Result] = {
    log.info(s"Running sandbox task [$id]...")
    val startMs = System.currentTimeMillis
    val result = call(app).map { r =>
      val res = SandboxTask.Result(this, "OK", r, (System.currentTimeMillis - startMs).toInt)
      log.info(s"Completed sandbox task [$id] with status [${res.status}] in [${res.elapsed}ms].")
      res
    }
    result
  }
  def call(app: Application): Future[String]
  override def toString = id
}

object SandboxTask extends Enum[SandboxTask] {
  case class Result(task: SandboxTask, status: String = "OK", result: String, elapsed: Int)

  case object Testbed extends SandboxTask("testbed", "Testbed", "A simple sandbox for messin' around.") {
    override def call(app: Application) = {
      Future.successful("All good!")
    }
  }

  override val values = findValues
}
