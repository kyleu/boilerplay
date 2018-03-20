package services.rest

import better.files.File
import models.rest.request.RequestFolder.PackageIndexFile
import models.rest.request.{RequestFolder, RestRequest}
import services.file.FileService
import util.Logging

object RestFolderOps extends Logging {
  private[this] def loadIndexJson(file: File, nameDefault: String) = FileService.getJsonContent(file).as[PackageIndexFile] match {
    case Right(pf) => Some(pf)
    case Left(_) => None
  }

  def fromDir(dir: File, loadRestRequest: File => RestRequest): RequestFolder = {
    val (folders, files) = dir.list.partition(_.isDirectory)
    val (packageFiles, requests) = files.partition(_.name == "index.json")
    val packageFile = packageFiles.toSeq.headOption.flatMap(x => loadIndexJson(x, dir.name))
    RequestFolder(
      name = dir.name,
      location = FileService.getDir("rest/request").relativize(dir).toString.stripPrefix("/"),
      title = packageFile.map(_.title).getOrElse(dir.name),
      description = packageFile.flatMap(_.description),
      author = packageFile.flatMap(_.author),
      folders = folders.map(fromDir(_, loadRestRequest)).toList,
      requests = requests.map(x => x.name.stripPrefix("/").stripSuffix(".json") -> loadRestRequest(x)).toList
    )
  }
}
