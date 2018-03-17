package services.rest

import better.files.File
import models.rest.{RequestBody, RestConfigSection, RestFolder, RestRequest}
import services.file.FileService
import util.JsonSerializers.Circe._
import util.Logging

object RestRepository extends Logging {
  private[this] lazy val dir = FileService.getDir("rest")
  private[this] var activeRepo: Option[RestRepository] = None

  implicit val jsonEncoder: Encoder[RestRepository] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestRepository] = deriveDecoder

  def reload() = {
    val repo = RestRepository(loadConfigs, loadRequests)
    activeRepo = Some(repo)
    repo
  }

  def repo = activeRepo.getOrElse(reload())

  def loadRequests = RestFolder.fromDir(dir / "request", loadRestRequest)

  private[this] def loadConfigs = {
    val (sections, files) = (dir / "config").list.partition(_.isDirectory)
    files.filterNot(_.name.startsWith("readme")).foreach(f => log.warn(s"Rest config files should be stored in a subdirectory (${f.path})."))
    sections.map(loadConfigSection).toIndexedSeq
  }

  private[this] def loadJson(file: File) = io.circe.parser.parse(file.contentAsString) match {
    case Right(json) => json
    case Left(x) => throw new IllegalStateException(s"Invalid json [${file.contentAsString}].", x)
  }

  private[this] def loadRestRequest(file: File) = loadJson(file).as[RestRequest] match {
    case Right(json) => json
    case Left(x) => RestRequest(
      name = "Parse Error",
      domain = x.getClass.getSimpleName.stripSuffix("$"),
      path = x.getMessage,
      body = Some(RequestBody.JsonContent(file.contentAsString))
    )
  }

  private[this] def loadConfigSection(section: File) = {
    val (entries, dirs) = section.list.partition(f => f.isRegularFile && f.name.endsWith(".json"))
    dirs.foreach(d => log.warn(s"Rest config sections only support json files (${d.path})."))
    val entriesMapped = entries.map { e =>
      e.name.stripSuffix(".json") -> (loadJson(e).as[Map[String, String]] match {
        case Right(x) => x
        case Left(x) => throw new IllegalStateException(s"[${section.name}/${e.name}}] does not contain a simple json object.")
      })
    }.toMap
    val defaultEntries = entriesMapped.getOrElse("defaults", throw new IllegalStateException(s"Config section [${section.name}] is missing [defaults.json]."))
    RestConfigSection(section.name, defaultEntries, entriesMapped.filterKeys(_ != "defaults"))
  }
}

case class RestRepository(configs: Seq[RestConfigSection], requests: RestFolder)
