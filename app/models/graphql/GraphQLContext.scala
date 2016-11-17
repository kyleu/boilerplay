package models.graphql

import models.user.User
import utils.Application

case class GraphQLContext(app: Application, user: User)
