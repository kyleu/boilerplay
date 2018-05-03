package services.process

import scala.sys.process._
import java.io.OutputStream

object ProcessService {
  case class Proc(
      cmd: Seq[String],
      out: String => Unit,
      err: String => Unit,
      log: String => Unit
  ) {
    val startMs = System.currentTimeMillis

    def run() = {
      def inHandler(in: OutputStream) = {}
      val p = cmd.run(new ProcessIO(
        in = inHandler,
        out = o => scala.io.Source.fromInputStream(o).getLines.foreach(out),
        err = e => scala.io.Source.fromInputStream(e).getLines.foreach(err)
      ))
      val exitCode = p.exitValue()
      log(s"Ran [${cmd.mkString(" ")}] in [${System.currentTimeMillis - startMs}ms] with exit code [$exitCode].")
    }

    def in(s: String) = {

    }
  }

  def main(args: Array[String]): Unit = {
    val cmd = args.toList match {
      case Nil => Seq("ls", "/")
      case c => c
    }
    val p = Proc(cmd, println _, println _, println _)
    println(p.run())
  }
}
