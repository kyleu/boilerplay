package services.rest

import better.files.File
import io.circe.Json
import models.rest.{RequestBody, RestRequest}
import services.file.FileService
import util.Logging

object RestRequestOps extends Logging {
  private[this] val dir = FileService.getDir("rest") / "request"

  def loadRestRequest(file: File) = {
    val p = dir.relativize(file.parent).toString
    FileService.getJsonContent(file).as[RestRequest] match {
      case Right(req) => if (req.path == p) {
        req
      } else {
        //throw new IllegalStateException(s"File [${req.name}] at path [$p] has invalid path [${req.path}].")
        val copy = req.copy(path = p)
        saveRequest(copy)
        copy
      }
      case Left(x) => RestRequest(
        name = file.name.stripSuffix(".json"),
        title = "Parse Error",
        domain = x.getClass.getSimpleName.stripSuffix("$"),
        path = p,
        body = Some(RequestBody.JsonContent(Json.obj(
          "status" -> Json.fromString("error"),
          "err" -> Json.fromString(x.getClass.getSimpleName),
          "message" -> Json.fromString(x.getMessage),
          "content" -> Json.fromString(file.contentAsString)
        ).spaces2))
      )
    }
  }

  def saveRequest(request: RestRequest) = {
    val f = dir / request.path / (request.name + "json")
    if (f.exists) {
      log.info(s"Overwriting request [${request.title}] at [${request.fullPath}].")
      f.delete()
    } else {
      log.info(s"Saving new request [${request.title}] to [${request.fullPath}].")
    }
    val json = io.circe.syntax.EncoderOps(request).asJson
    f.writeText(json.spaces2 + "\n")
    request
  }
}
