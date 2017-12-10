package models.sandbox

import enumeratum.{Enum, EnumEntry, CirceEnum}
import models.Application
import services.ServiceRegistry
import services.database.BackupRestore
import util.FutureUtils.defaultContext
import util.Logging
import util.tracing.TraceData

import scala.concurrent.Future

sealed abstract class SandboxTask(val id: String, val name: String, val description: String) extends EnumEntry with Logging {
  def run(app: Application, services: ServiceRegistry, arg: Option[String])(implicit trace: TraceData): Future[SandboxTask.Result] = {
    app.tracing.trace(id + ".sandbox") { sandboxTrace =>
      log.info(s"Running sandbox task [$id]...")
      val startMs = System.currentTimeMillis
      val result = call(app, services, arg)(sandboxTrace).map { r =>
        val res = SandboxTask.Result(this, arg, "OK", r, (System.currentTimeMillis - startMs).toInt)
        log.info(s"Completed sandbox task [$id] with status [${res.status}] in [${res.elapsed}ms].")
        res
      }
      result
    }
  }
  def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData): Future[String]
  override def toString = id
}

object SandboxTask extends Enum[SandboxTask] with CirceEnum[SandboxTask] {
  case class Result(task: SandboxTask, arg: Option[String], status: String = "OK", result: String, elapsed: Int)

  case object Testbed extends SandboxTask("testbed", "Testbed", "A simple sandbox for messing around.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      Future.successful("All good!")
    }
  }

  case object TracingTest extends SandboxTask("tracing", "Tracing Test", "A tracing test.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = TracingLogic.go(app, argument)
  }

  case object WebCall extends SandboxTask("webCall", "Web Call", "Calls the provided url and returns stats.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = argument match {
      case Some(url) => app.ws.url("sandbox.request", url).get().map { rsp =>
        s"${rsp.status} $url (${util.NumberUtils.withCommas(rsp.bodyAsBytes.size)} bytes)"
      }
      case None => Future.successful("No url provided...")
    }
  }

  case object JsonSerialization extends SandboxTask("jsonSerialization", "Json Serialization", "Serializes 1,000 messages with circe.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      val pong = models.Pong(util.DateUtils.now)
      val startMs = util.DateUtils.nowMillis
      val json = (0 to 1000).map(_ => util.JsonSerializers.writeResponseMessage(pong))
      Future.successful(s"Json (${json.size}): " + (util.DateUtils.nowMillis - startMs) + "ms")
    }
  }

  case object BinarySerialization extends SandboxTask("binarySerialization", "Binary Serialization", "Serializes 1,000 messages with BooPickle.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      val pong = models.Pong(util.DateUtils.now)
      val startMs = util.DateUtils.nowMillis
      val binary = (0 to 1000).map(_ => util.BinarySerializers.writeResponseMessage(pong))
      Future.successful(s"Binary (${binary.size}): " + (util.DateUtils.nowMillis - startMs) + "ms")
    }
  }

  case object CopyTable extends SandboxTask("copyTable", "Copy Table", "Copy a database table, in preparation for new DDL.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      argument.map(a => TableLogic.copyTable(app, a)).getOrElse(Future.successful("Argument required."))
    }
  }

  case object RestoreTable extends SandboxTask("restoreTable", "Restore Table", "Restores a database table.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      argument.map(a => TableLogic.restoreTable(app, a)).getOrElse(Future.successful("Argument required."))
    }
  }

  case object DatabaseBackup extends SandboxTask("databaseBackup", "Database Backup", "Backs up the database.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      Future.successful(BackupRestore.backup())
    }
  }

  case object DatabaseRestore extends SandboxTask("databaseRestore", "Database Restore", "Restores the database.") {
    override def call(app: Application, services: ServiceRegistry, argument: Option[String])(implicit trace: TraceData) = {
      Future.successful(BackupRestore.restore("TODO"))
    }
  }

  override val values = findValues
}
