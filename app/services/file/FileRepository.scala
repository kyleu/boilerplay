package services.file

object FileRepository {
  def list(key: String, path: Option[String] = None) = {
    val d = path.map(FileService.getDir(key) / _).getOrElse(FileService.getDir(key))
    if (!d.isDirectory || !d.isReadable) {
      throw new IllegalStateException(s"Cannot read [$key] subdirectory at path [${d.pathAsString}].")
    }
    d.list.filter(_.isDirectory).map(_.name).toIndexedSeq -> d.list.filter(_.isRegularFile).map(_.name).toIndexedSeq
  }

  def readFile(key: String, path: String) = {
    val f = FileService.getDir(key) / path
    if (!f.isRegularFile || !f.isReadable) {
      throw new IllegalStateException(s"Cannot read [$key] file at path [${f.pathAsString}].")
    }
    f.contentAsString
  }

  def readJson(key: String, path: String) = {
    util.JsonSerializers.parseJson(readFile(key, path)) match {
      case Right(json) => json
      case Left(x) => throw x
    }
  }

  def writeFile(key: String, path: String, content: String) = {
    val f = FileService.getDir(key) / path
    f.writeText(content)
  }
}
