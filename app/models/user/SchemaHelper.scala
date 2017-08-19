package models.user

import models.graphql.GraphQLContext
import util.tracing.TraceData
import zipkin.Endpoint

import scala.concurrent.Future

abstract class SchemaHelper(val name: String) {
  private[this] lazy val endpoint = Endpoint.builder().serviceName(name + ".schema").build()

  protected def trace[A](ctx: GraphQLContext, k: String)(f: TraceData => Future[A])(implicit trace: TraceData) = {
    ctx.app.tracing.traceFuture(k) { tn =>
      tn.span.remoteEndpoint(endpoint)
      f(tn)
    }
  }
}
