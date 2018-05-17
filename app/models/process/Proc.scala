package models.process

import java.io.OutputStream

import scala.sys.process._

case class Proc(cmd: Seq[String], out: String => Unit, err: String => Unit, log: String => Unit) {
  private[this] var inputStream: Option[OutputStream] = None

  def run() = {
    val startMs = System.currentTimeMillis
    def inHandler(in: OutputStream) = inputStream = Some(in)
    val p = cmd.run(new ProcessIO(
      in = inHandler,
      out = o => scala.io.Source.fromInputStream(o).getLines.foreach(out),
      err = e => scala.io.Source.fromInputStream(e).getLines.foreach(err)
    ))
    val exitCode = p.exitValue()
    val dur = System.currentTimeMillis - startMs
    log(s"Ran [${cmd.mkString(" ")}] in [${dur}ms] with exit code [$exitCode].")
    exitCode -> dur
  }

  def in(s: String) = inputStream.foreach(_.write(s.getBytes))
}
