package util.web

import io.circe.Json
import play.api.data.FormError
import play.api.mvc.{Accepting, AnyContent, Request}

object ControllerUtils {
  val acceptsCsv = Accepting("text/csv")

  def getForm(request: Request[AnyContent]) = request.body.asFormUrlEncoded match {
    case Some(f) => f.mapValues(x => x.headOption.getOrElse(throw new IllegalStateException("Empty form element.")))
    case None => throw new IllegalStateException("Missing form post.")
  }

  def errorsToString(errors: Seq[FormError]) = errors.map(e => e.key + ": " + e.message).mkString(", ")

  def jsonBody(body: AnyContent) = body.asJson.map { json =>
    import sangria.marshalling.MarshallingUtil._
    import sangria.marshalling.circe._
    import sangria.marshalling.playJson._
    json.convertMarshaled[io.circe.Json]
  }.getOrElse(throw new IllegalStateException("Http post with json body required."))

  def jsonFormOrBody(body: AnyContent, key: String) = {
    body.asFormUrlEncoded.map(_(key).head).map(io.circe.parser.parse).map(_.right.get).getOrElse(jsonBody(body))
  }

  def jsonObject(json: Json) = json.asObject.getOrElse(throw new IllegalStateException("Json is not an object."))

  def jsonArguments(body: AnyContent, arguments: String*) = {
    val json = jsonObject(jsonFormOrBody(body, "arguments"))
    arguments.map(arg => json(arg) match {
      case Some(argJson) => arg -> argJson
      case None => throw new IllegalStateException(s"Missing argument [$arg] in body.")
    }).toMap
  }
}
