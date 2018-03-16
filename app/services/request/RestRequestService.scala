package services.request

import models.request.RestRequest
import play.api.libs.ws.WSClient

@javax.inject.Singleton
class RestRequestService @javax.inject.Inject() (ws: WSClient) {
  def call(req: RestRequest, log: String => Unit) = {
    log(s"Calling [${req.url}] with method [${req.method}], content type [${req.contentType}}], and a body of size [${req.bodySize}].")
  }
}
