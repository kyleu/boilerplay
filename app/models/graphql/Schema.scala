package models.graphql

import com.kyleu.projectile.graphql.GraphQLContext
import com.kyleu.projectile.models.graphql.BaseGraphQLSchema
import sangria.schema._
import util.Version

import scala.concurrent.Future

object Schema extends BaseGraphQLSchema {
  override protected def additionalFetchers = {
    Nil ++
      /* Start model fetchers */
      /* End model fetchers */
      Nil
  }

  override protected def additionalQueryFields = fields[GraphQLContext, Unit](
    Field(name = "status", fieldType = StringType, resolve = c => Future.successful("OK")),
    Field(name = "version", fieldType = StringType, resolve = _ => Future.successful(Version.version))
  ) ++
    /* Start query fields */
    /* End query fields */
    Nil

  override protected def additionalMutationFields = {
    Nil ++
      /* Start mutation fields */
      /* End mutation fields */
      Nil
  }
}
