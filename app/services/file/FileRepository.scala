package services.file

object FileRepository {
  def list(key: String, path: Option[String] = None) = {
    val keyDir = FileService.getDir(key)
    val d = path.map(keyDir / _).getOrElse(keyDir)
    if (!d.isDirectory || !d.isReadable) {
      throw new IllegalStateException(s"Cannot read [$key] subdirectory at path [${d.pathAsString}].")
    }
    d.list.filter(_.isDirectory).map(_.name).toIndexedSeq -> d.list.filter(_.isRegularFile).map(_.name).toIndexedSeq
  }

  def readFile(key: String, path: String) = {
    val keyDir = FileService.getDir(key)
    val d = keyDir / path
    if (!d.isRegularFile || !d.isReadable) {
      throw new IllegalStateException(s"Cannot read [$key] file at path [${d.pathAsString}].")
    }
    d.contentAsString
  }

  def readJson(key: String, path: String) = {
    import io.circe.parser._
    parse(readFile(key, path)) match {
      case Right(json) => json
      case Left(x) => throw x
    }
  }
}
