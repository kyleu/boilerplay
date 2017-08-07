package models.user

import java.util.UUID
import models.graphql.{CommonSchema, GraphQLContext}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.macros.derive._
import sangria.schema._
import services.user.UserService

object UserSchema {
  implicit val userId = HasId[User, UUID](_.id)

  val userByIdFetcher = Fetcher((_: GraphQLContext, idSeq: Seq[UUID]) => UserService.getByIdSeq(idSeq))

  implicit lazy val userType: ObjectType[GraphQLContext, User] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "user",
    fieldType = ListType(userType),
    arguments = queryArg :: limitArg :: offsetArg :: Nil,
    resolve = c => c.arg(CommonSchema.queryArg) match {
      case Some(q) => UserService.search(q, None, c.arg(limitArg), c.arg(offsetArg))
      case _ => UserService.getAll(None, c.arg(limitArg), c.arg(offsetArg))
    }
  ))
}
