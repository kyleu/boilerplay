package services.rest

import better.files.File
import services.file.FileService
import util.Logging

object RestRepository {
  private[this] var activeRepo: Option[RestRepository] = None

  def reload() = {
    val repo = new RestRepository
    activeRepo = Some(repo)
    repo
  }

  def repo = activeRepo.getOrElse(reload())
}

class RestRepository extends Logging {
  private lazy val dir = FileService.getDir("rest")

  lazy val requests = RestRepositorySerialization.Folder.fromDir(dir / "request")

  lazy val configs = {
    val (sections, files) = (dir / "config").list.partition(_.isDirectory)
    files.filterNot(_.name.startsWith("readme")).foreach(f => log.warn(s"Rest config files should be stored in a subdirectory (${f.path})."))
    sections.map(RestRepositorySerialization.loadConfigSection).toIndexedSeq
  }
}
