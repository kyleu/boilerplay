package models.graphql

import models.Application
import models.user.User
import util.tracing.TraceData

case class GraphQLContext(app: Application, user: User, implicit val trace: TraceData)
