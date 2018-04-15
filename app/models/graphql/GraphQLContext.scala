package models.graphql

import models.Application
import models.auth.Credentials
import services.ServiceRegistry
import services.graphql.GraphQLService
import util.tracing.TraceData

final case class GraphQLContext(app: Application, svc: GraphQLService, services: ServiceRegistry, creds: Credentials, trace: TraceData)
