package services.file

import better.files.File
import com.kyleu.projectile.util.tracing.TraceData
import com.kyleu.projectile.util.{JsonSerializers, Logging}

object FileService extends Logging {
  private[this] var dataDir: Option[File] = None

  def setRootDir(d: File) = {
    dataDir = Some(d)

    if ((!d.exists) || (!d.isDirectory)) {
      log.warn(s"Cannot load data directory [${d.pathAsString}].")(TraceData.noop)
      log.warn("To set an alternate file cache directory, set [data.directory] in your configuration.")(TraceData.noop)
    }
  }

  def getDir(name: String, createIfMissing: Boolean = true) = {
    val d = dataDir.getOrElse(throw new IllegalStateException("File service not initialized.")) / name
    if (createIfMissing && (!d.exists)) {
      d.createDirectory
    }
    d
  }

  def getContent(f: File) = f.contentAsString

  def getJsonContent(f: File) = JsonSerializers.parseJson(getContent(f)) match {
    case Right(json) => json
    case Left(x) => throw new IllegalStateException(s"Invalid json for file [${f.pathAsString}]: ${getContent(f)}]", x)
  }
}
