package services.rest

import better.files.File
import io.circe.Json
import models.rest.http.{RestBody, RestUrl}
import models.rest.request.RestRequest
import services.file.FileService
import util.Logging

object RestRequestOps extends Logging {
  private[this] val dir = FileService.getDir("rest") / "request"

  def loadRestRequest(file: File) = {
    val p = dir.relativize(file.parent).toString
    FileService.getJsonContent(file).as[RestRequest] match {
      case Right(req) => if (req.fileLocation == p) {
        req
      } else {
        //throw new IllegalStateException(s"File [${req.name}] at path [$p] has invalid path [${req.path}].")
        val copy = req.copy(url = req.url.copy(path = p))
        saveRequest(copy)
        copy
      }
      case Left(x) => RestRequest(
        name = file.name.stripSuffix(".json"),
        title = "Parse Error",
        url = RestUrl(domain = x.getClass.getSimpleName.stripSuffix("$"), path = p),
        body = Some(RestBody.JsonContent(Json.obj(
          "status" -> Json.fromString("error"),
          "err" -> Json.fromString(x.getClass.getSimpleName),
          "message" -> Json.fromString(x.getMessage),
          "content" -> Json.fromString(file.contentAsString)
        ).spaces2))
      )
    }
  }

  def saveRequest(request: RestRequest) = {
    val f = dir / request.url.path / (request.name + "json")
    if (f.exists) {
      log.info(s"Overwriting request [${request.title}] at [${request.fileLocation}].")
      f.delete()
    } else {
      log.info(s"Saving new request [${request.title}] to [${request.fileLocation}].")
    }
    val json = io.circe.syntax.EncoderOps(request).asJson
    f.writeText(json.spaces2 + "\n")
    request
  }
}
