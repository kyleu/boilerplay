package models.rest

import better.files.File
import services.file.FileService
import util.JsonSerializers._

object RestFolder {
  implicit val jsonEncoder: Encoder[RestFolder] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestFolder] = deriveDecoder

  case class PackageIndexFile(title: String, description: Option[String] = None, author: Option[String] = None)

  object PackageIndexFile {
    implicit val jsonEncoder: Encoder[PackageIndexFile] = deriveEncoder
    implicit val jsonDecoder: Decoder[PackageIndexFile] = deriveDecoder
  }

  private[this] def loadIndexJson(file: File, nameDefault: String) = FileService.getJsonContent(file).as[PackageIndexFile] match {
    case Right(pf) => Some(pf)
    case Left(_) => None
  }

  def fromDir(dir: File, loadRestRequest: File => RestRequest): RestFolder = {
    val (folders, files) = dir.list.partition(_.isDirectory)
    val (packageFiles, requests) = files.partition(_.name == "index.json")
    val packageFile = packageFiles.toSeq.headOption.flatMap(x => loadIndexJson(x, dir.name))
    RestFolder(
      name = dir.name,
      path = FileService.getDir("rest/request").relativize(dir).toString.stripPrefix("/"),
      title = packageFile.map(_.title).getOrElse(dir.name),
      description = packageFile.flatMap(_.description),
      author = packageFile.flatMap(_.author),
      folders = folders.map(RestFolder.fromDir(_, loadRestRequest)).toList,
      requests = requests.map(x => x.name.stripPrefix("/").stripSuffix(".json") -> loadRestRequest(x)).toList
    )
  }
}

case class RestFolder(
    name: String,
    path: String,
    title: String,
    description: Option[String],
    author: Option[String],
    folders: Seq[RestFolder] = Nil,
    requests: Seq[(String, RestRequest)] = Nil
) {
  val pathNormalized = path.replaceAllLiterally("/", "-")
  val isRoot = path.isEmpty

  def getResource(path: String): Either[RestFolder, RestRequest] = {
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
