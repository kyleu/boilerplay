package models.graphql

import models.user.User
import util.Application
import util.tracing.TraceData

case class GraphQLContext(app: Application, user: User, implicit val trace: TraceData)
