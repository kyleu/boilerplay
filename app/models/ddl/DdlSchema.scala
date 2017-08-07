package models.ddl

import models.graphql.{CommonSchema, GraphQLContext}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.macros.derive._
import sangria.schema._
import services.ddl.DdlService

object DdlSchema {
  implicit val ddlId = HasId[Ddl, Int](_.id)

  val ddlByIdFetcher = Fetcher((_: GraphQLContext, idSeq: Seq[Int]) => DdlService.getByIdSeq(idSeq))

  implicit lazy val ddlType: ObjectType[GraphQLContext, Ddl] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "ddl",
    fieldType = ListType(ddlType),
    arguments = queryArg :: limitArg :: offsetArg :: Nil,
    resolve = c => c.arg(CommonSchema.queryArg) match {
      case Some(q) => DdlService.search(q, None, c.arg(limitArg), c.arg(offsetArg))
      case _ => DdlService.getAll(None, c.arg(limitArg), c.arg(offsetArg))
    }
  ))
}
