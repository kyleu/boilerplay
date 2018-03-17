package services.rest

import better.files.File
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import models.rest.{RestConfigSection, RestRequest}
import util.Logging

object RestRepositorySerialization extends Logging {
  def loadJson(file: File) = io.circe.parser.parse(file.contentAsString) match {
    case Right(json) => json
    case Left(x) => throw new IllegalStateException(s"Invalid json [${file.contentAsString}].", x)
  }

  def loadRestRequest(file: File) = loadJson(file).as[RestRequest] match {
    case Right(json) => json
    case Left(x) => throw new IllegalStateException(s"Json for [${file.name}] is not a valid RestRequest [${file.contentAsString}].", x)
  }

  def loadConfigSection(section: File) = {
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

  object Folder {
    implicit val jsonEncoder: Encoder[Folder] = deriveEncoder
    implicit val jsonDecoder: Decoder[Folder] = deriveDecoder

    def fromDir(dir: File): Folder = {
      val (folders, requests) = dir.list.partition(_.isDirectory)
      val requestsMapped = requests.map(x => x.name.stripSuffix(".json") -> loadRestRequest(x)).toSeq
      Folder(name = dir.name, folders = folders.map(Folder.fromDir).toSeq, requests = requestsMapped)
    }
  }

  case class Folder(name: String, folders: Seq[Folder] = Nil, requests: Seq[(String, RestRequest)] = Nil) {
    def get(path: String): RestRequest = path match {
      case x if x.contains('/') =>
        val n = x.substring(0, x.indexOf('/'))
        folders.find(_.name == n).getOrElse(throw new IllegalStateException(s"No folder named [$n]")).get(x.substring(x.indexOf('/')))
      case _ => requests.find(_._1 == path.stripSuffix(".json")).getOrElse(throw new IllegalStateException(s"No request file named [$path]"))._2
    }
  }
}
