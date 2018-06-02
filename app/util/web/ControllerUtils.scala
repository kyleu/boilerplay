package util.web

import play.api.data.FormError
import play.api.mvc.{AnyContent, Request}
import util.JsonSerializers.Json
import util.tracing.TraceData
import zipkin.TraceKeys

object ControllerUtils {
  def getForm(body: AnyContent, prefix: Option[String] = None) = body.asFormUrlEncoded match {
    case Some(f) =>
      val fullMap = f.mapValues(x => x.headOption.getOrElse(throw new IllegalStateException("Empty form element.")))
      prefix.map(p => fullMap.filterKeys(_.startsWith(p)).map(x => x._1.stripPrefix(p) -> x._2)).getOrElse(fullMap)
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
    val content = body.asFormUrlEncoded.map(_(key).headOption.getOrElse(throw new IllegalStateException(s"Missing form field [$key].")))
    content.map(util.JsonSerializers.parseJson).map(_.right.get).getOrElse(jsonBody(body))
  }

  def jsonObject(json: Json) = json.asObject.getOrElse(throw new IllegalStateException("Json is not an object."))

  def jsonArguments(body: AnyContent, arguments: String*) = {
    val json = jsonObject(jsonFormOrBody(body, "arguments"))
    arguments.map(arg => json(arg) match {
      case Some(argJson) => arg -> argJson
      case None => throw new IllegalStateException(s"Missing argument [$arg] in body.")
    }).toMap
  }

  def enhanceRequest(request: Request[AnyContent], trace: TraceData) = {
    trace.tag(TraceKeys.HTTP_REQUEST_SIZE, request.body.asText.map(_.length).orElse(request.body.asRaw.map(_.size.toInt)).getOrElse(0).toString)
  }
}
