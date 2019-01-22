/* Generated File */
package models.graphql.auth

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.auth.{Oauth2InfoRow, Oauth2InfoRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object Oauth2InfoRowSchema extends GraphQLSchemaHelper("oauth2InfoRow") {
  implicit val oauth2InfoRowPrimaryKeyId: HasId[Oauth2InfoRow, (String, String)] = HasId[Oauth2InfoRow, (String, String)](x => (x.provider, x.key))
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[(String, String)]) = {
    c.injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val oauth2InfoRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val oauth2InfoRowProviderArg = Argument("provider", StringType)
  val oauth2InfoRowProviderSeqArg = Argument("providers", ListInputType(StringType))
  val oauth2InfoRowKeyArg = Argument("key", StringType)
  val oauth2InfoRowKeySeqArg = Argument("keys", ListInputType(StringType))

  implicit lazy val oauth2InfoRowType: sangria.schema.ObjectType[GraphQLContext, Oauth2InfoRow] = deriveObjectType()

  implicit lazy val oauth2InfoRowResultType: sangria.schema.ObjectType[GraphQLContext, Oauth2InfoRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "oauth2InfoRow", desc = None, t = OptionType(oauth2InfoRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).getByPrimaryKey(c.ctx.creds, c.arg(oauth2InfoRowProviderArg), c.arg(oauth2InfoRowKeyArg))(td)
    }, oauth2InfoRowProviderArg, oauth2InfoRowKeyArg),
    unitField(name = "oauth2InfoRowSearch", desc = None, t = oauth2InfoRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[services.auth.Oauth2InfoRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val oauth2InfoRowMutationType = ObjectType(
    name = "Oauth2InfoRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(oauth2InfoRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(oauth2InfoRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).update(c.ctx.creds, c.arg(oauth2InfoRowProviderArg), c.arg(oauth2InfoRowKeyArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, oauth2InfoRowProviderArg, oauth2InfoRowKeyArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = oauth2InfoRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).remove(c.ctx.creds, c.arg(oauth2InfoRowProviderArg), c.arg(oauth2InfoRowKeyArg))(td)
      }, oauth2InfoRowProviderArg, oauth2InfoRowKeyArg)
    )
  )

  val mutationFields = fields(unitField(name = "oauth2InfoRow", desc = None, t = oauth2InfoRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[Oauth2InfoRow]) = {
    Oauth2InfoRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
