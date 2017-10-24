package models.graphql

import models.Application
import models.auth.Credentials
import services.ServiceRegistry
import util.tracing.TraceData

case class GraphQLContext(app: Application, services: ServiceRegistry, creds: Credentials, trace: TraceData)
