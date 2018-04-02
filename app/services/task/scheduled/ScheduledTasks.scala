package services.task.scheduled

import models.auth.Credentials
import models.task.scheduled.ScheduledTask
import services.sync.SyncService
import util.FutureUtils.defaultContext
import util.tracing.TraceData

import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

@javax.inject.Singleton
class ScheduledTasks @javax.inject.Inject() (
    helloWorldTask: HelloWorldTask,
    syncService: SyncService
) {
  val all = Seq[ScheduledTask](helloWorldTask)

  def byKey(key: String) = all.find(_.key == key).getOrElse(throw new IllegalStateException(s"No task with key [$key]."))

  def runAll(creds: Credentials, tasks: Seq[ScheduledTask], log: String => Unit)(implicit td: TraceData) = {
    Future.sequence(tasks.map(t => run(creds, t, log).map(t -> _))).map(_.toMap)
  }

  def run(creds: Credentials, task: ScheduledTask, log: String => Unit)(implicit td: TraceData) = {
    val start = util.DateUtils.nowMillis
    syncService.set(creds, task.key, "Running", "Started processing").flatMap { _ =>
      Try(task.run(creds, log)) match {
        case Success(r) => r.flatMap { ret =>
          val msg = s"Completed task [${task.key}] in [${util.DateUtils.nowMillis - start}ms]."
          log(msg)
          syncService.set(creds, task.key, "OK", msg).map(_ => ret)
        }.recoverWith {
          case NonFatal(x) =>
            val msg = s"Failed future for [${x.getClass.getSimpleName}] while running task [${task.key}]. ${x.getMessage}"
            log(msg)
            syncService.set(creds, task.key, "Error", msg).map(_ => false)
        }
        case Failure(x) =>
          val msg = s"Error [${x.getClass.getSimpleName}] while running task [${task.key}]. ${x.getMessage}"
          log(msg)
          syncService.set(creds, task.key, "Error", msg).map(_ => false)
      }
    }
  }
}
