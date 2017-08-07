package models.user

import models.graphql.{CommonSchema, GraphQLContext}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.macros.derive._
import sangria.schema._
import services.user.PasswordInfoService

object PasswordInfoSchema {
  implicit val passwordInfoId = HasId[PasswordInfo, (String, String)](x => (x.provider, x.key))

  val passwordInfoByIdFetcher = Fetcher((_: GraphQLContext, idSeq: Seq[(String, String)]) => PasswordInfoService.getByIdSeq(idSeq))

  implicit lazy val passwordInfoType: ObjectType[GraphQLContext, PasswordInfo] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "passwordInfo",
    fieldType = ListType(passwordInfoType),
    arguments = queryArg :: limitArg :: offsetArg :: Nil,
    resolve = c => c.arg(CommonSchema.queryArg) match {
      case Some(q) => PasswordInfoService.search(q, None, c.arg(limitArg), c.arg(offsetArg))
      case _ => PasswordInfoService.getAll(None, c.arg(limitArg), c.arg(offsetArg))
    }
  ))
}
