package services.rest

import better.files.File
import models.rest.RestConfigSection
import services.file.FileService
import util.Logging

object RestConfigOps extends Logging {
  def loadConfigSection(section: File) = {
    val (entries, dirs) = section.list.partition(f => f.isRegularFile && f.name.endsWith(".json"))
    dirs.foreach(d => log.warn(s"Rest config sections only support json files (${d.path})."))
    val entriesMapped = entries.map { e =>
      e.name.stripSuffix(".json") -> (FileService.getJsonContent(e).as[Map[String, String]] match {
        case Right(x) => x
        case Left(x) => throw new IllegalStateException(s"[${section.name}/${e.name}}] does not contain a simple json object.", x)
      })
    }.toMap
    val defaultEntries = entriesMapped.getOrElse("defaults", throw new IllegalStateException(s"Config section [${section.name}] is missing [defaults.json]."))
    RestConfigSection(section.name, defaultEntries, entriesMapped.filterKeys(_ != "defaults"))
  }
}
