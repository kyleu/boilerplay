package models.graphql

import models.Application
import services.graphql.GraphQLService
import util.tracing.TraceData

final case class GraphQLContext(app: Application, svc: GraphQLService, trace: TraceData)
