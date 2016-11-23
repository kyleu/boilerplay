package services.file

import utils.Logging

object FileService extends Logging {
  private[this] var dataDir: Option[java.io.File] = None

  def setRootDir(d: java.io.File) = {
    dataDir = Some(d)

    if ((!d.exists) || (!d.isDirectory)) {
      log.warn(s"Cannot load data directory [${d.getAbsolutePath}].")
      log.warn(s"To set an alternate file cache directory, set [data.directory] in your configuration.")
    }
  }

  def getDir(name: String, createIfMissing: Boolean = true) = {
    val d = new java.io.File(dataDir.getOrElse(throw new IllegalStateException("File service not initialized.")), name)
    if (createIfMissing && (!d.exists)) {
      d.mkdir
    }
    d
  }
}
