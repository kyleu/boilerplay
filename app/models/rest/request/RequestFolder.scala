package models.rest.request

import util.JsonSerializers._

object RequestFolder {
  implicit val jsonEncoder: Encoder[RequestFolder] = deriveEncoder
  implicit val jsonDecoder: Decoder[RequestFolder] = deriveDecoder

  case class PackageIndexFile(title: String, description: Option[String] = None, author: Option[String] = None)

  object PackageIndexFile {
    implicit val jsonEncoder: Encoder[PackageIndexFile] = deriveEncoder
    implicit val jsonDecoder: Decoder[PackageIndexFile] = deriveDecoder
  }
}

case class RequestFolder(
    name: String,
    location: String,
    title: String,
    description: Option[String],
    author: Option[String],
    folders: Seq[RequestFolder] = Nil,
    requests: Seq[(String, RestRequest)] = Nil
) {
  val pathNormalized = location.replaceAllLiterally("/", "-")
  val isRoot = location.isEmpty

  def getResource(path: String): Either[RequestFolder, RestRequest] = {
    path.stripPrefix("/").stripSuffix(".json") match {
      case x if x.contains('/') =>
        val n = x.substring(0, x.indexOf('/'))
        folders.find(_.name == n).getOrElse(throw new IllegalStateException(s"No folder [$n] available in [$x].")).getResource(x.substring(x.indexOf('/')))
      case x => folders.find(_.name == x).map(Left.apply).getOrElse {
        requests.find(_._1 == x).map(_._2).map(Right.apply).getOrElse {
          val availFolders = folders.map(_.name).mkString(", ")
          val availFiles = requests.map(_._1).mkString(", ")
          throw new IllegalStateException(s"No resource named [$path]. Available folders: [$availFolders] files: [$availFiles].")
        }
      }
    }
  }

  def getFile(path: String) = getResource(path).right.getOrElse(throw new IllegalStateException(" is a folder, not a request file."))
  def getFolder(path: String) = getResource(path).left.getOrElse(throw new IllegalStateException(" is a request file, not a folder."))
}
