package services.process

import java.util.UUID

import models.auth.Credentials
import models.process.CachedProc
import models.user.Role

object ProcessService {
  private[this] val activeProcesses = collection.mutable.HashMap.empty[UUID, CachedProc]
  private[this] val completedProcesses = collection.mutable.HashMap.empty[UUID, CachedProc]

  def getActive = activeProcesses.values.toIndexedSeq.sortBy(p => p.started.map(util.DateUtils.toMillis)).reverse

  def getProc(id: UUID) = activeProcesses.get(id).orElse(completedProcesses.get(id)).getOrElse {
    throw new IllegalStateException(s"Cannot find process with id [$id].")
  }

  def isAllowed(creds: Credentials, cmd: Seq[String]) = creds.user.role.qualifies(Role.Admin)

  def start(creds: Credentials, cmd: Seq[String], onOutput: CachedProc.Output => Unit, onComplete: (Int, Long) => Unit, async: Boolean = false) = {
    if (!isAllowed(creds, cmd)) {
      throw new IllegalStateException(s"User [${creds.user.id}] is not allowed to run command [${cmd.mkString(" ")}].")
    }
    val p = CachedProc(cmd, onOutput, onComplete)
    activeProcesses(p.id) = p
    if (async) {
      new Thread(() => p.run())
    } else {
      p.run()
    }
    p
  }

  def main(args: Array[String]): Unit = {
    val cmd = if (args.isEmpty) { Seq("ls", "/") } else { args.toSeq }
    start(
      creds = Credentials.system,
      cmd = cmd,
      onOutput = o => println(o),
      onComplete = (e, d) => println(s"[res] ${util.DateUtils.now} - Completed in [${d}ms] with exit code [$e].")
    )
  }
}
