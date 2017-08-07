package models

import models.graphql.{CommonSchema, GraphQLContext}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.macros.derive._
import sangria.schema._
import services.SettingValuesService

object SettingValuesSchema {
  implicit val settingValuesId = HasId[SettingValues, String](_.k)

  val settingValuesByKFetcher = Fetcher((_: GraphQLContext, kSeq: Seq[String]) => SettingValuesService.getByIdSeq(kSeq))

  implicit lazy val settingValuesType: ObjectType[GraphQLContext, SettingValues] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "settingValues",
    fieldType = ListType(settingValuesType),
    arguments = queryArg :: limitArg :: offsetArg :: Nil,
    resolve = c => c.arg(CommonSchema.queryArg) match {
      case Some(q) => SettingValuesService.search(q, None, c.arg(limitArg), c.arg(offsetArg))
      case _ => SettingValuesService.getAll(None, c.arg(limitArg), c.arg(offsetArg))
    }
  ))
}
