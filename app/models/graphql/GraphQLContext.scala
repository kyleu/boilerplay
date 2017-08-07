package models.graphql

import models.user.RichUser
import util.Application

case class GraphQLContext(app: Application, user: RichUser)
