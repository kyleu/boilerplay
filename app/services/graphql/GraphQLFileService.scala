package services.graphql

import play.api.libs.ws.WSClient
import services.file.FileRepository

@javax.inject.Singleton
class GraphQLFileService @javax.inject.Inject() (ws: WSClient) {
  def read(path: String) = FileRepository.readFile("graphql", path)

  def save(path: String, query: String) = FileRepository.writeFile("graphql", path, query)

  def call(query: String, log: String => Unit) = {
    log(s"Calling GraphQL request with a body of size [${query.length}].")
  }
}
