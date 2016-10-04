package services.translation.api

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

@javax.inject.Singleton
class YandexApi @javax.inject.Inject() (ws: WSClient) extends ApiProvider("Yandex") {
  val apiKey = "trnsl.1.1.20160807T203728Z.3db5b35613fb236a.f1895c3d286efe675f44143d84dfdcf1ace08eb6"
  def url(lang: String) = {
    val newLang = lang match {
      case "iw" => "he"
      case x => x
    }
    s"https://translate.yandex.net/api/v1.5/tr.json/translate?key=$apiKey&lang=en-$newLang"
  }

  private[this] def body(text: String) = {
    Map("text" -> Seq(text))
  }

  override def translate(lang: String, key: String, text: String) = {
    ws.url(url(lang)).withHeaders("Accept" -> "application/json").post(body(text)).map { response =>
      val json = Json.parse(response.body)
      val t = (json \ "code").as[Int] match {
        case 200 => (json \ "text").as[Seq[String]].head
        case _ => (json \ "message").as[String]
      }
      Some(key -> t)
    }
  }
}
