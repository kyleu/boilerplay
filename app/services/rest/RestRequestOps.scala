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
      case Right(req) => req
      case Left(x) => RestRequest(
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

  def saveRequest(location: String, request: RestRequest) = {
    val f = location.lastIndexOf('/') match {
      case -1 => dir / (location + ".json")
      case idx => dir / location.substring(0, idx) / (location.substring(idx + 1) + ".json")
    }
    if (f.exists) {
      log.info(s"Overwriting request [${request.title}] at [$location].")
      f.delete()
    } else {
      log.info(s"Saving new request [${request.title}] to [$location].")
    }
    val json = io.circe.syntax.EncoderOps(request).asJson
    f.writeText(json.spaces2 + "\n")
    RestRepository.reload(Some("request/" + location))
    RestRepository.getRequest(location)
  }

  def fromForm(r: RestRequest, form: Map[String, String]) = r.copy(
    title = form.getOrElse("title", r.title)
  )
}
