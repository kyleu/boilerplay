package services.rest

import models.rest.config.RestConfigSection
import models.rest.request.{RequestFolder, RestRequest}
import services.file.FileService
import util.JsonSerializers._
import util.Logging

object RestRepository extends Logging {
  private[this] lazy val dir = FileService.getDir("rest")
  private[this] var activeRepo: Option[RestRepository] = None

  implicit val jsonEncoder: Encoder[RestRepository] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestRepository] = deriveDecoder

  def repo: RestRepository = activeRepo.getOrElse(reload())

  def getRequest(location: String) = repo.requests.getRequest(location)
  def getFolder(location: String) = repo.requests.getFolder(location)
  def getResource(location: String) = repo.requests.getResource(location)

  def reload(path: Option[String] = None): RestRepository = {
    val r = path match {
      case Some(p) => RestRepository(repo.configs, loadRequests) // TODO Reload only p
      case None => RestRepository(loadConfigs, loadRequests)
    }
    activeRepo = Some(r)
    r
  }

  private[this] def loadRequests = RestFolderOps.fromDir(dir / "request", RestRequestOps.loadRestRequest)

  private[this] def loadConfigs = {
    val (sections, files) = (dir / "config").list.partition(_.isDirectory)
    files.filterNot(_.name.startsWith("readme")).foreach(f => log.warn(s"Rest config files should be stored in a subdirectory (${f.path})."))
    sections.map(RestConfigOps.loadConfigSection).toIndexedSeq
  }
}

case class RestRepository(configs: Seq[RestConfigSection], requests: RequestFolder)
