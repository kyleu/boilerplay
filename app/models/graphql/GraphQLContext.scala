package models.graphql

import models.user.User
import util.Application

case class GraphQLContext(app: Application, user: User)
