package services.task

import java.util.UUID

import akka.actor.ActorSystem
import models.Configuration
import models.auth.Credentials
import models.task.ScheduledTaskRun
import models.task.scheduled.{ScheduledTask, ScheduledTaskOutput}
import services.sync.SyncService
import services.task.scheduled.ScheduledTasks
import util.FutureUtils.serviceContext
import util.Logging
import util.tracing.{TraceData, TracingService}
import util.JsonSerializers._

import scala.concurrent.Future
import scala.util.control.NonFatal

@javax.inject.Singleton
class ScheduledTaskService @javax.inject.Inject() (
    config: Configuration,
    runService: ScheduledTaskRunService,
    scheduledTasks: ScheduledTasks,
    tracingService: TracingService,
    syncService: SyncService
) extends Logging {
  def initSchedule(system: ActorSystem, creds: Credentials, args: Seq[String], delaySecs: Int = 5, intervalSecs: Int = 15)(implicit td: TraceData): Unit = {
    if (config.scheduledTaskEnabled) {
      import scala.concurrent.duration._
      log.info(s"Scheduling task to run every [$intervalSecs] seconds, after an initial [$delaySecs] second delay.")
      system.scheduler.schedule(delaySecs.seconds, intervalSecs.seconds, (() => runAll(creds, args)): Runnable)
    } else {
      log.info("Scheduled task is disabled.")
    }
  }

  def runAll(creds: Credentials = Credentials.system, args: Seq[String] = Nil)(implicit td: TraceData) = {
    start(creds, scheduledTasks.all, args)
  }

  def runSingle(creds: Credentials = Credentials.system, task: ScheduledTask, args: Seq[String] = Nil)(implicit td: TraceData) = {
    start(creds, Seq(task), args)
  }

  private[this] def start(creds: Credentials = Credentials.system, tasks: Seq[ScheduledTask], args: Seq[String] = Nil)(implicit td: TraceData) = {
    tracingService.trace("scheduledTaskService.run") { trace =>
      val id = UUID.randomUUID
      val f = syncService.progressSvc.getByKeySeq(creds, tasks.map(_.key)).flatMap { syncs =>
        val tasksToRun = if (args.contains("force")) {
          tasks
        } else {
          val now = util.DateUtils.now
          tasks.filter(t => syncs.find(_.key == t.key) match {
            case Some(_) if args.contains("merge") => false
            case Some(_) if args.contains("concurrent") => true
            case Some(sync) if sync.status == "Running" =>
              val last = util.DateUtils.toMillis(sync.lastTime)
              val diffMinutes = (util.DateUtils.nowMillis - last) / 1000 / 60
              if (diffMinutes > 60) {
                log.warn(s"Reseting running scheduled task [${t.key}] as it has been running for over an hour.")
                syncService.set(creds, t.key, "Reset", "Timed out running task.")
              } else {
                log.debug(s"Already running scheduled task [${t.key}], and [concurrent] or [merge] was not provided.")
              }
              false
            case Some(sync) if sync.lastTime.plusSeconds(t.runFrequencySeconds.toLong).isBefore(now) => true
            case Some(sync) =>
              log.debug(s"Skipping task [${t.key}], as it was run at [${sync.lastTime}], which is within [${t.runFrequencySeconds}] seconds.")
              false
            case None => true
          })
        }

        if (tasksToRun.isEmpty) {
          Future.successful(Nil)
        } else {
          log.debug(s"Running scheduled tasks [${tasksToRun.map(_.key).mkString(", ")}].")
          Future.sequence(tasksToRun.map { task =>
            go(id, creds, args, task, trace).map { o =>
              ScheduledTaskRun(id = id, task = task.key, arguments = args, status = o.status, output = o.asJson, started = o.start, completed = o.end)
            }
          })
        }
      }

      f.flatMap { runs =>
        if (args.contains("persist")) {
          Future.sequence(runs.map { run =>
            runService.insert(creds, run).map(_.getOrElse(throw new IllegalStateException(s"Could not find inserted run [$id]."))).recover {
              case NonFatal(_) => run.copy(status = "SaveError")
            }
          })
        } else {
          Future.successful(runs)
        }
      }
    }
  }

  private[this] def go(id: UUID, creds: Credentials, args: Seq[String], task: ScheduledTask, td: TraceData) = {
    val start = util.DateUtils.now
    val startMs = util.DateUtils.toMillis(start)

    val logs = collection.mutable.ArrayBuffer.empty[ScheduledTaskOutput.Log]
    def addLog(msg: String) = logs += ScheduledTaskOutput.Log(msg, (util.DateUtils.nowMillis - startMs).toInt)

    addLog(s"Starting scheduled task run [$id:${task.key}] at [$start]...")

    def fin(status: String) = {
      val ret = ScheduledTaskOutput(
        userId = creds.user.id, username = creds.user.username,
        status = status, logs = logs,
        start = start, end = util.DateUtils.now
      )
      log.debug(s"Completed scheduled task [${task.key}] with args [${args.mkString(", ")}] in [${ret.durationMs}ms].")
      ret
    }

    scheduledTasks.run(creds, task, addLog)(td).map { ok =>
      addLog(s"Completed scheduled task run [$id].")
      fin(if (ok) { "Ok" } else { "Error" })
    }.recover {
      case NonFatal(x) =>
        addLog(s"Error encountered for scheduled task run [$id].")
        addLog(s" - ${x.getClass.getSimpleName}: ${x.getMessage}")
        x.printStackTrace()
        fin("Error")
    }
  }
}
