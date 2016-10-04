package services.translation.api

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WSClient

@javax.inject.Singleton
class BingApi @javax.inject.Inject() (ws: WSClient) extends ApiProvider("Yandex") {
  val apiKey = "9UxlZvokcbn6ICSy1lRf3I2B+nQoes4AcQODLrlBBEY"
  def url(lang: String, text: String) = s"http://api.microsofttranslator.com/V2/Http.svc/Translate?appId=Bearer $apiKey&from=en&to=$lang&text=$text"

  override def translate(lang: String, key: String, text: String) = {
    ws.url(url(lang, text)).get().map { response =>
      Some(key -> response.body)
    }
  }
}
