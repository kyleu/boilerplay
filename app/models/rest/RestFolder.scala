package models.rest

import better.files.File
import util.JsonSerializers.Circe._

object RestFolder {
  implicit val jsonEncoder: Encoder[RestFolder] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestFolder] = deriveDecoder

  def fromDir(dir: File, loadRestRequest: File => RestRequest): RestFolder = {
    val (folders, requests) = dir.list.partition(_.isDirectory)
    val requestsMapped = requests.map(x => x.name.stripSuffix(".json") -> loadRestRequest(x)).toSeq
    RestFolder(name = dir.name, folders = folders.map(RestFolder.fromDir(_, loadRestRequest)).toSeq, requests = requestsMapped)
  }
}

case class RestFolder(name: String, folders: Seq[RestFolder] = Nil, requests: Seq[(String, RestRequest)] = Nil) {
  def get(path: String): RestRequest = path match {
    case x if x.contains('/') =>
      val n = x.substring(0, x.indexOf('/'))
      folders.find(_.name == n).getOrElse(throw new IllegalStateException(s"No folder named [$n]")).get(x.substring(x.indexOf('/')))
    case _ => requests.find(_._1 == path.stripSuffix(".json")).getOrElse {
      throw new IllegalStateException(s"No request file named [$path]. Available: [${requests.map(_._1).mkString(", ")}]")
    }._2
  }
}
