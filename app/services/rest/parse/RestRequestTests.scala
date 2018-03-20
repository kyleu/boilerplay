package services.rest.parse

import models.rest.http.{RestBody, RestHeader}
import models.rest.request.RestRequest
import services.file.FileService

import scala.concurrent.Future

object RestRequestTests {
  def run(arg: Option[String]) = arg match {
    case None => Future.successful("Send [raw], [curl], or [reload] as an argument to begin.")
    case Some("raw") => testRaw()
    case Some("curl") => testCurl()
    case Some(x) => throw new IllegalStateException(s"Invalid argument [$x].")
  }

  private[this] def testRaw() = {
    val src = (FileService.getDir("test") / "raw.txt").contentAsString
    val parsed = HttpParser.parse("test", "Test Request", src)
    val json = io.circe.syntax.EncoderOps(parsed).asJson.spaces2
    val tgt = RestRequestSerializers.toRaw(parsed)
    val status = if (src.trim != tgt.trim) {
      s"Source differs from target (${src.trim.length} != ${tgt.trim.length})."
    } else {
      "OK"
    }
    Future.successful(s"Status: $status\n\n$json\n\nSrc:\n$src\n\nTgt:\n$tgt")
  }

  private[this] def testCurl() = {
    val src = (FileService.getDir("test") / "curl.txt").contentAsString
    val req = RestRequest(
      name = "CurlTest",
      title = "Curl Test",
      headers = Seq(RestHeader("foo", "bar")),
      body = Some(RestBody.JsonContent("{\n  \"x\": \"y\"\n}"))
    )
    val json = io.circe.syntax.EncoderOps(req).asJson.spaces2
    val tgt = RestRequestSerializers.toCurl(req)
    val status = if (src.trim != tgt.trim) {
      s"Source differs from target (${src.trim.length} != ${tgt.trim.length})."
    } else {
      "OK"
    }
    Future.successful(s"Status: $status\n\n$json\n\nSrc:\n$src\n\nTgt:\n$tgt")
  }
}
