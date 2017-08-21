package models.sandbox

import enumeratum._
import models.Application
import util.FutureUtils.defaultContext
import util.Logging
import util.tracing.TraceData

import scala.concurrent.Future

sealed abstract class SandboxTask(val id: String, val name: String, val description: String) extends EnumEntry with Logging {
  def run(app: Application, arg: Option[String])(implicit trace: TraceData): Future[SandboxTask.Result] = {
    app.tracing.trace(id + ".sandbox") { sandboxTrace =>
      log.info(s"Running sandbox task [$id]...")
      val startMs = System.currentTimeMillis
      val result = call(app, arg)(sandboxTrace).map { r =>
        val res = SandboxTask.Result(this, arg, "OK", r, (System.currentTimeMillis - startMs).toInt)
        log.info(s"Completed sandbox task [$id] with status [${res.status}] in [${res.elapsed}ms].")
        res
      }
      result
    }
  }
  def call(app: Application, argument: Option[String])(implicit trace: TraceData): Future[String]
  override def toString = id
}

object SandboxTask extends Enum[SandboxTask] with CirceEnum[SandboxTask] {
  case class Result(task: SandboxTask, arg: Option[String], status: String = "OK", result: String, elapsed: Int)

  case object Testbed extends SandboxTask("testbed", "Testbed", "A simple sandbox for messin' around.") {
    override def call(app: Application, argument: Option[String])(implicit trace: TraceData) = {
      Future.successful("All good!")
    }
  }

  case object TracingTest extends SandboxTask("tracing", "Tracing Test", "A tracing test.") {
    override def call(app: Application, argument: Option[String])(implicit trace: TraceData) = TracingLogic.go(app, argument)
  }

  case object WebCall extends SandboxTask("webcall", "Web Call", "Calls the provided url and returns stats.") {
    override def call(app: Application, argument: Option[String])(implicit trace: TraceData) = argument match {
      case Some(url) => app.ws.url("sandbox.request", url).get().map { rsp =>
        s"${rsp.status} $url (${util.NumberUtils.withCommas(rsp.bodyAsBytes.size)} bytes)"
      }
      case None => Future.successful("No url provided...")
    }
  }

  override val values = findValues
}
