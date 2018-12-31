package models.sandbox

import com.google.inject.Injector
import enumeratum.{CirceEnum, Enum, EnumEntry}
import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.auth.UserCredentials
import services.database.BackupRestore
import com.kyleu.projectile.util.JsonSerializers._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

sealed abstract class SandboxTask(val id: String, val name: String, val description: String) extends EnumEntry with Logging {
  def run(cfg: SandboxTask.Config)(implicit trace: TraceData): Future[SandboxTask.Result] = {
    cfg.tracingService.trace(id + ".sandbox") { sandboxTrace =>
      log.info(s"Running sandbox task [$id]...")
      val startMs = System.currentTimeMillis
      val result = call(cfg)(sandboxTrace).map { r =>
        val res = SandboxTask.Result(this, cfg.argument, "OK", r, (System.currentTimeMillis - startMs).toInt)
        log.info(s"Completed sandbox task [$id] with status [${res.status}] in [${res.elapsed}ms].")
        res
      }
      result
    }
  }
  def call(cfg: SandboxTask.Config)(implicit trace: TraceData): Future[String]
  override def toString = id
}

object SandboxTask extends Enum[SandboxTask] with CirceEnum[SandboxTask] {
  final case class Config(tracingService: TracingService, injector: Injector, argument: Option[String])

  final case class Result(task: SandboxTask, arg: Option[String], status: String = "OK", result: String, elapsed: Int)

  object Result {
    implicit val jsonEncoder: Encoder[Result] = deriveEncoder
    implicit val jsonDecoder: Decoder[Result] = deriveDecoder
  }

  case object Testbed extends SandboxTask("testbed", "Testbed", "A simple sandbox for messing around.") {
    override def call(cfg: Config)(implicit trace: TraceData) = {
      Future.successful("OK")
    }
  }

  case object CopyTable extends SandboxTask("copyTable", "Copy Table", "Copy a database table, in preparation for new DDL.") {
    override def call(cfg: Config)(implicit trace: TraceData) = {
      cfg.argument.map(a => TableLogic.copyTable(a)).getOrElse(Future.successful("Argument required."))
    }
  }

  case object RestoreTable extends SandboxTask("restoreTable", "Restore Table", "Restores a database table.") {
    override def call(cfg: Config)(implicit trace: TraceData) = {
      cfg.argument.map(a => TableLogic.restoreTable(a)).getOrElse(Future.successful("Argument required."))
    }
  }

  case object DatabaseBackup extends SandboxTask("databaseBackup", "Database Backup", "Backs up the database.") {
    override def call(cfg: Config)(implicit trace: TraceData) = {
      Future.successful(BackupRestore.backup())
    }
  }

  case object DatabaseRestore extends SandboxTask("databaseRestore", "Database Restore", "Restores the database.") {
    override def call(cfg: Config)(implicit trace: TraceData) = {
      Future.successful(BackupRestore.restore("TODO"))
    }
  }

  case object Graphdoc extends SandboxTask("graphdoc", "Graphdoc", "Runs graphdoc against this project's schema.") {
    override def call(cfg: Config)(implicit trace: TraceData) = {
      GraphdocLogic.generate(UserCredentials.system, cfg.injector, cfg.argument)
    }
  }

  override val values = findValues
}

