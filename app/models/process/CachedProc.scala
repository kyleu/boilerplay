package models.process

import java.time.LocalDateTime
import java.util.UUID

object CachedProc {
  case class Output(level: String, line: String, occurred: LocalDateTime) {
    override def toString = s"[$level] $occurred - $line"
  }
  case class Status(cmd: Seq[String], outLines: Int, errLines: Int, logLines: Int, started: LocalDateTime, lastActivity: LocalDateTime, exitCode: Option[Int])
}

case class CachedProc(cmd: Seq[String], onOutput: CachedProc.Output => Unit, onComplete: (Int, Long) => Unit) {
  val id = UUID.randomUUID

  private[this] var startMs: Option[Long] = None
  private[this] var isRunning = false

  private[this] val output = collection.mutable.ArrayBuffer.empty[CachedProc.Output]
  def getOutput = output.toIndexedSeq

  private[this] def callback(level: String, line: String) = {
    val o = CachedProc.Output(level, line, util.DateUtils.now)
    output += o
    onOutput(o)
  }

  val proc = Proc(
    cmd = cmd,
    out = callback("out", _),
    err = callback("err", _),
    log = callback("log", _)
  )

  def started = startMs.map(util.DateUtils.fromMillis)

  def run() = {
    startMs = Some(System.currentTimeMillis)
    isRunning = true
    val (exitCode, duration) = proc.run()
    isRunning = false
    onComplete(exitCode, duration)
    exitCode
  }
}
