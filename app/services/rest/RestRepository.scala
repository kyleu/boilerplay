package services.rest

import better.files.File
import models.rest.config.RestConfigSection
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
  lazy val dir = FileService.getDir("rest")

  lazy val configs = {
    val (sections, files) = (dir / "config").list.partition(_.isDirectory)
    files.filterNot(_.name.startsWith("readme")).foreach(f => log.warn(s"Rest config files should be stored in a subdirectory (${dir.path})."))
    sections.map(loadConfigSection)
  }

  private[this] def loadConfigSection(section: File) = section.list.map { f =>
    val (entries, dirs) = section.list.partition(f => f.isRegularFile && f.name.endsWith(".json"))
    dirs.foreach(f => log.warn(s"Rest config sections only support json files (${dir.path})."))
    val entriesMapped = entries.map { e =>
      val json = io.circe.parser.parse(e.contentAsString).toOption.getOrElse(throw new IllegalStateException(s"Invalid json [${e.contentAsString}]."))
      e.name.stripSuffix(".json") -> (json.as[Map[String, String]] match {
        case Right(x) => x
        case Left(x) => throw new IllegalStateException(s"[${section.name}/${e.name}}] does not contain a simple json object.")
      })
    }.toMap
    val defaultEntries = entriesMapped.getOrElse("defaults", throw new IllegalStateException(s"Config section [${section.name}] is missing [defaults.json]."))
    RestConfigSection(section.name, defaultEntries, entriesMapped.filterKeys(_ != "defaults"))
  }
}
