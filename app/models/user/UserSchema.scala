package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.graphql.{CommonSchema, GraphQLContext, SchemaHelper}
import sangria.macros.derive._
import sangria.schema._
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import models.template.Theme
import sangria.execution.deferred.{Fetcher, HasId}
import models.result.paging.PagingOptions
import util.FutureUtils.graphQlContext

import scala.concurrent.Future

object UserSchema extends SchemaHelper("user") {
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

  val userPrimaryKeyId = HasId[User, UUID](_.id)
  val userByPrimaryKeyFetcher = Fetcher { (c: GraphQLContext, idSeq: Seq[UUID]) =>
    c.app.userService.getByPrimaryKeySeq(idSeq)(c.trace)
  }(userPrimaryKeyId)

  val userByRoleFetcher = Fetcher { (c: GraphQLContext, roleSeq: Seq[Role]) =>
    c.app.userService.getByRoleSeq(roleSeq)(c.trace)
  }(HasId[User, Role](_.role))

  implicit val loginInfoType = deriveObjectType[GraphQLContext, LoginInfo](ObjectTypeDescription("Information about login credentials."))
  implicit val userPreferenceType = deriveObjectType[GraphQLContext, UserPreferences](ObjectTypeDescription("Information about users of the system."))
  implicit lazy val userType: ObjectType[GraphQLContext, User] = deriveObjectType()

  implicit lazy val userResultType: ObjectType[GraphQLContext, UserResult] = deriveObjectType()

  private[this] def toResult(r: SearchResult[User]) = {
    UserResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }

  val queryFields = fields[GraphQLContext, Unit](
    Field(
      name = "profile",
      description = Some("Returns information about the currently logged in user."),
      fieldType = profileType,
      resolve = c => trace(c.ctx, "profile") { _ =>
        val u = c.ctx.user
        Future.successful(UserProfile(u.id, u.username, u.profile.providerKey, u.role, u.preferences.theme, u.created))
      }
    ),
    Field(
      name = "user",
      fieldType = userResultType,
      arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
      resolve = c => trace(c.ctx, "search")(implicit timing => runSearch(c.ctx.app.userService, c).map(toResult))
    )
  )
}
