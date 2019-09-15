/* Generated File */
package models.graphql.customer

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.customer.{LanguageRow, LanguageRowResult}
import models.graphql.film.FilmRowSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object LanguageRowSchema extends GraphQLSchemaHelper("languageRow") {
  implicit val languageRowPrimaryKeyId: HasId[LanguageRow, Int] = HasId[LanguageRow, Int](_.languageId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.customer.LanguageRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val languageRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val languageRowLanguageIdArg = Argument("languageId", IntType)
  val languageRowLanguageIdSeqArg = Argument("languageIds", ListInputType(IntType))

  val languageRowNameArg = Argument("name", StringType)
  val languageRowNameSeqArg = Argument("names", ListInputType(StringType))
  val languageRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val languageRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  implicit lazy val languageRowType: sangria.schema.ObjectType[GraphQLContext, LanguageRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "films",
        fieldType = ListType(FilmRowSchema.filmRowType),
        resolve = c => FilmRowSchema.filmRowByLanguageIdFetcher.deferRelSeq(
          FilmRowSchema.filmRowByLanguageIdRelation, c.value.languageId
        )
      ),
      Field(
        name = "filmsByOriginal",
        fieldType = ListType(FilmRowSchema.filmRowType),
        resolve = c => FilmRowSchema.filmRowByOriginalLanguageIdFetcher.deferRelSeq(
          FilmRowSchema.filmRowByOriginalLanguageIdRelation, Some(c.value.languageId)
        )
      )
    )
  )

  implicit lazy val languageRowResultType: sangria.schema.ObjectType[GraphQLContext, LanguageRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "languageRow", desc = None, t = OptionType(languageRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.LanguageRowService].getByPrimaryKey(c.ctx.creds, c.arg(languageRowLanguageIdArg))(td)
    }, languageRowLanguageIdArg),
    unitField(name = "languageRowSeq", desc = None, t = ListType(languageRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.LanguageRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(languageRowLanguageIdSeqArg))(td)
    }, languageRowLanguageIdSeqArg),
    unitField(name = "languageRowSearch", desc = None, t = languageRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.customer.LanguageRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "languagesByName", desc = None, t = ListType(languageRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.LanguageRowService].getByName(c.ctx.creds, c.arg(languageRowNameArg))(td)
    }, languageRowNameArg),
    unitField(name = "languagesByNameSeq", desc = None, t = ListType(languageRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.LanguageRowService].getByNameSeq(c.ctx.creds, c.arg(languageRowNameSeqArg))(td)
    }, languageRowNameSeqArg),
    unitField(name = "languagesByLastUpdate", desc = None, t = ListType(languageRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.LanguageRowService].getByLastUpdate(c.ctx.creds, c.arg(languageRowLastUpdateArg))(td)
    }, languageRowLastUpdateArg),
    unitField(name = "languagesByLastUpdateSeq", desc = None, t = ListType(languageRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.LanguageRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(languageRowLastUpdateSeqArg))(td)
    }, languageRowLastUpdateSeqArg)
  )

  val languageRowMutationType = ObjectType(
    name = "LanguageRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(languageRowType), f = (c, td) => {
        c.ctx.getInstance[services.customer.LanguageRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(languageRowType), f = (c, td) => {
        c.ctx.getInstance[services.customer.LanguageRowService].update(c.ctx.creds, c.arg(languageRowLanguageIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, languageRowLanguageIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = languageRowType, f = (c, td) => {
        c.ctx.getInstance[services.customer.LanguageRowService].remove(c.ctx.creds, c.arg(languageRowLanguageIdArg))(td)
      }, languageRowLanguageIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "languageRow", desc = None, t = languageRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[LanguageRow]) = {
    LanguageRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
