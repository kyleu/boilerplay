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
    val json = FileService.getJsonContent(file)
    json.as[RestRequest] match {
      case Right(req) => if (req.fileLocation == p) {
        req
      } else {
        //throw new IllegalStateException(s"File [${req.name}] at path [$p] has invalid path [${req.path}].")
        val copy = req.copy(folder = Some(p))
        saveRequest(copy)
        copy
      }
      case Left(x) => RestRequest(
        name = file.name.stripSuffix(".json"),
        folder = Some(p),
        title = json.asObject.flatMap(_.apply("title").map(_.as[String].map(_ + " (error)").getOrElse("No Title"))).getOrElse("Parse Error"),
        description = Some("[Parse Error]"),
        url = RestUrl(path = p),
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
    val f = dir / request.url.path / (request.name + ".json")
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
