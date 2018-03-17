package services.rest

import models.rest.RestRequest
import play.api.libs.ws.WSClient

@javax.inject.Singleton
class RestRequestService @javax.inject.Inject() (ws: WSClient) {
  def call(req: RestRequest, log: String => Unit) = {
    val bodySize = req.body.map(b => s", and a body of size [${b.size}].)")
    log(s"Calling [${req.url}] with method [${req.method}], content type [${req.contentType}}]$bodySize.")
  }
}
