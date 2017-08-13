package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import models.template.Theme
import sangria.execution.deferred.{Fetcher, HasId}
import models.result.filter.FilterSchema.reportFiltersArg
import models.result.orderBy.OrderBySchema.orderBysArg
import models.result.paging.PagingOptions
import util.FutureUtils.graphQlContext

object UserSchema {
  implicit val roleEnum = CommonSchema.deriveEnumeratumType(
    name = "Role",
    description = "The role of the system user.",
    values = Role.values.map(t => t -> t.entryName).toList
  )

  implicit val themeEnum = CommonSchema.deriveStringEnumeratumType(
    name = "Theme",
    description = "The selected theme color.",
    values = Theme.values.map(t => t -> t.color).toList
  )

  implicit val profileType = deriveObjectType[GraphQLContext, UserProfile](ObjectTypeDescription("Information about the current session."))

  implicit val userId = HasId[User, UUID](_.id)
  val userByIdFetcher = Fetcher((c: GraphQLContext, idSeq: Seq[UUID]) => c.app.userService.getByIdSeq(idSeq))

  implicit val loginInfoType = deriveObjectType[GraphQLContext, LoginInfo](ObjectTypeDescription("Information about login credentials."))
  implicit val userPreferenceType = deriveObjectType[GraphQLContext, UserPreferences](ObjectTypeDescription("Information about users of the system."))
  implicit lazy val userType: ObjectType[GraphQLContext, User] = deriveObjectType()

  implicit lazy val userResultType: ObjectType[GraphQLContext, UserResult] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](
    Field(
      name = "profile",
      description = Some("Returns information about the currently logged in user."),
      fieldType = profileType,
      resolve = c => {
        val u = c.ctx.user
        UserProfile(u.id, u.username, u.profile.providerKey, u.role, u.preferences.theme, u.created)
      }
    ),
    Field(
      name = "user",
      fieldType = userResultType,
      arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
      resolve = c =>
      {
        val start = util.DateUtils.now
        val filters = c.arg(reportFiltersArg).getOrElse(Nil)
        val orderBys = c.arg(orderBysArg).getOrElse(Nil)
        val limit = c.arg(limitArg)
        val offset = c.arg(offsetArg)
        val f = c.arg(CommonSchema.queryArg) match {
          case Some(q) => c.ctx.app.userService.searchWithCount(q, filters, orderBys, limit, offset)
          case _ => c.ctx.app.userService.getAllWithCount(filters, orderBys, limit, offset)
        }
        f.map { r =>
          val paging = PagingOptions.from(r._1, limit, offset)
          val durationMs = (System.currentTimeMillis - util.DateUtils.toMillis(start)).toInt
          UserResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = r._1, results = r._2, durationMs = durationMs)
        }
      }
    )
  )
}
