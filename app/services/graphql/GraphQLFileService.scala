package services.graphql

import models.request.GraphQLRequest
import play.api.libs.ws.WSClient
import services.file.FileRepository

@javax.inject.Singleton
class GraphQLFileService @javax.inject.Inject() (ws: WSClient) {
  def read(path: String) = FileRepository.readJson("graphql", path).as[GraphQLRequest] match {
    case Right(r) => r
    case Left(x) => throw x
  }

  def call(req: GraphQLRequest, log: String => Unit) = {
    log(s"Calling GraphQL request [${req.name}], with variables [${req.variablesJson}] and a body of size [${req.bodySize}].")
  }
}
