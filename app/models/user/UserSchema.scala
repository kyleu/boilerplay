package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.template.Theme
import sangria.execution.deferred.{Fetcher, HasId}

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
      fieldType = ListType(userType),
      arguments = queryArg :: limitArg :: offsetArg :: Nil,
      resolve = c => c.arg(CommonSchema.queryArg) match {
        case Some(q) => c.ctx.app.userService.search(q, c.arg(limitArg), c.arg(offsetArg))
        case _ => c.ctx.app.userService.getAll(c.arg(limitArg), c.arg(offsetArg))
      }
    )
  )
}
