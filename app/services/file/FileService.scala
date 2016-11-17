package services.file

object FileService {
  private[this] var dataDir: Option[java.io.File] = None

  def setRootDir(d: java.io.File) = {
    dataDir = Some(d)

    if ((!d.exists) || (!d.isDirectory)) {
      throw new IllegalStateException(s"Cannot load data directory [${d.getAbsolutePath}].")
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
