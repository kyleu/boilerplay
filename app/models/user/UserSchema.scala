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

  val queryFields = fields[GraphQLContext, Unit](
    Field(
      name = "profile",
      description = Some("Returns information about the currently logged in user."),
      fieldType = profileType,
      resolve = c => trace(c.ctx, "profile") { _ =>
        val u = c.ctx.user
        Future.successful(UserProfile(u.id, u.username, u.profile.providerKey, u.role, u.preferences.theme, u.created))
      }(c.ctx.trace)
    ),
    Field(
      name = "user",
      fieldType = userResultType,
      arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
      resolve = c => trace(c.ctx, "search") { implicit timing =>
      val start = util.DateUtils.now
      val filters = c.arg(reportFiltersArg).getOrElse(Nil)
      val orderBys = c.arg(orderBysArg).getOrElse(Nil)
      val limit = c.arg(limitArg)
      val offset = c.arg(offsetArg)
      val f = c.arg(CommonSchema.queryArg) match {
        case Some(q) => c.ctx.app.userService.searchWithCount(q, filters, orderBys, limit, offset)(c.ctx.trace)
        case _ => c.ctx.app.userService.getAllWithCount(filters, orderBys, limit, offset)(c.ctx.trace)
      }
      f.map { r =>
        timing.span.annotate("Composing result.")
        val paging = PagingOptions.from(r._1, limit, offset)
        val durationMs = (System.currentTimeMillis - util.DateUtils.toMillis(start)).toInt
        UserResult(paging = paging, filters = filters, orderBys = orderBys, totalCount = r._1, results = r._2, durationMs = durationMs)
      }
    }(c.ctx.trace)
    )
  )
}
