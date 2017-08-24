package models.graphql

import models.Application
import models.user.User
import services.ServiceRegistry
import util.tracing.TraceData

case class GraphQLContext(app: Application, services: ServiceRegistry, user: User, trace: TraceData)
