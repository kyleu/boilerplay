/* Generated File */
package models.graphql.auth

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.auth.{PasswordInfoRow, PasswordInfoRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object PasswordInfoRowSchema extends GraphQLSchemaHelper("passwordInfoRow") {
  implicit val passwordInfoRowPrimaryKeyId: HasId[PasswordInfoRow, (String, String)] = HasId[PasswordInfoRow, (String, String)](x => (x.provider, x.key))
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[(String, String)]) = {
    c.injector.getInstance(classOf[services.auth.PasswordInfoRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val passwordInfoRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val passwordInfoRowProviderArg = Argument("provider", StringType)
  val passwordInfoRowProviderSeqArg = Argument("providers", ListInputType(StringType))
  val passwordInfoRowKeyArg = Argument("key", StringType)
  val passwordInfoRowKeySeqArg = Argument("keys", ListInputType(StringType))

  implicit lazy val passwordInfoRowType: sangria.schema.ObjectType[GraphQLContext, PasswordInfoRow] = deriveObjectType()

  implicit lazy val passwordInfoRowResultType: sangria.schema.ObjectType[GraphQLContext, PasswordInfoRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "passwordInfoRow", desc = None, t = OptionType(passwordInfoRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.PasswordInfoRowService]).getByPrimaryKey(c.ctx.creds, c.arg(passwordInfoRowProviderArg), c.arg(passwordInfoRowKeyArg))(td)
    }, passwordInfoRowProviderArg, passwordInfoRowKeyArg),
    unitField(name = "passwordInfoRowSearch", desc = None, t = passwordInfoRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[services.auth.PasswordInfoRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val passwordInfoRowMutationType = ObjectType(
    name = "PasswordInfoRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(passwordInfoRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.PasswordInfoRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(passwordInfoRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.PasswordInfoRowService]).update(c.ctx.creds, c.arg(passwordInfoRowProviderArg), c.arg(passwordInfoRowKeyArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, passwordInfoRowProviderArg, passwordInfoRowKeyArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = passwordInfoRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.PasswordInfoRowService]).remove(c.ctx.creds, c.arg(passwordInfoRowProviderArg), c.arg(passwordInfoRowKeyArg))(td)
      }, passwordInfoRowProviderArg, passwordInfoRowKeyArg)
    )
  )

  val mutationFields = fields(unitField(name = "passwordInfoRow", desc = None, t = passwordInfoRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[PasswordInfoRow]) = {
    PasswordInfoRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
