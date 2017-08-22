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
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import util.FutureUtils.graphQlContext

import scala.concurrent.Future

object UserSchema extends SchemaHelper("user") {
  implicit val roleEnum = CommonSchema.deriveEnumeratumType(
    name = "RoleEnum",
    description = "The role of the system user.",
    values = Role.values.map(t => t -> t.entryName).toList
  )

  implicit val themeEnum = CommonSchema.deriveStringEnumeratumType(
    name = "ThemeEnum",
    description = "The selected theme color.",
    values = Theme.values.map(t => t -> t.color).toList
  )

  implicit val profileType = deriveObjectType[GraphQLContext, UserProfile](ObjectTypeDescription("Information about the current session."))

  implicit val userPrimaryKeyId = HasId[User, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = c.app.userService.getByPrimaryKeySeq(idSeq)(c.trace)
  val userByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val userByRoleRelation = Relation[User, Role]("byRole", x => Seq(x.role))
  val userByRoleFetcher = Fetcher.rel[GraphQLContext, User, User, UUID](
    getByPrimaryKeySeq, (c, rels) => c.app.userService.getByRoleSeq(rels(userByRoleRelation))(c.trace)
  )

  implicit val loginInfoType = deriveObjectType[GraphQLContext, LoginInfo](ObjectTypeDescription("Information about login credentials."))
  implicit val userPreferenceType = deriveObjectType[GraphQLContext, UserPreferences](ObjectTypeDescription("Information about users of the system."))
  implicit lazy val userType: ObjectType[GraphQLContext, User] = deriveObjectType()

  implicit lazy val userResultType: ObjectType[GraphQLContext, UserResult] = deriveObjectType()

  private[this] def toResult(r: SearchResult[User]) = {
    UserResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }

  val roleArg = Argument("role", roleEnum, description = "Filters the results to a provided SandboxTask.")

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
      resolve = c => trace(c.ctx, "search")(td => runSearch(c.ctx.app.userService, c, td).map(toResult))
    ),
    Field(
      name = "userByRole",
      fieldType = ListType(userType),
      arguments = roleArg :: Nil,
      resolve = c => userByRoleFetcher.deferRelSeq(userByRoleRelation, c.arg(roleArg))
    )
  )
}
