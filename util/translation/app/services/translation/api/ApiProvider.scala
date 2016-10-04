package services.translation.api

import scala.concurrent.Future

abstract class ApiProvider(s: String) {
  def translate(lang: String, key: String, text: String): Future[Option[(String, String)]]
}
