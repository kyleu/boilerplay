/* Generated File */
package models.graphql.address

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.address.{CountryRow, CountryRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object CountryRowSchema extends GraphQLSchemaHelper("countryRow") {
  implicit val countryRowPrimaryKeyId: HasId[CountryRow, Int] = HasId[CountryRow, Int](_.countryId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.address.CountryRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val countryRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val countryRowCountryIdArg = Argument("countryId", IntType)
  val countryRowCountryIdSeqArg = Argument("countryIds", ListInputType(IntType))

  val countryRowCountryArg = Argument("country", StringType)
  val countryRowCountrySeqArg = Argument("countrys", ListInputType(StringType))
  val countryRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val countryRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  implicit lazy val countryRowType: sangria.schema.ObjectType[GraphQLContext, CountryRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "cities",
        fieldType = ListType(CityRowSchema.cityRowType),
        resolve = c => CityRowSchema.cityRowByCountryIdFetcher.deferRelSeq(
          CityRowSchema.cityRowByCountryIdRelation, c.value.countryId
        )
      )
    )
  )

  implicit lazy val countryRowResultType: sangria.schema.ObjectType[GraphQLContext, CountryRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "countryRow", desc = None, t = OptionType(countryRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CountryRowService].getByPrimaryKey(c.ctx.creds, c.arg(countryRowCountryIdArg))(td)
    }, countryRowCountryIdArg),
    unitField(name = "countryRowSeq", desc = None, t = ListType(countryRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CountryRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(countryRowCountryIdSeqArg))(td)
    }, countryRowCountryIdSeqArg),
    unitField(name = "countryRowSearch", desc = None, t = countryRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.address.CountryRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "countriesByCountry", desc = None, t = ListType(countryRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CountryRowService].getByCountry(c.ctx.creds, c.arg(countryRowCountryArg))(td)
    }, countryRowCountryArg),
    unitField(name = "countriesByCountrySeq", desc = None, t = ListType(countryRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CountryRowService].getByCountrySeq(c.ctx.creds, c.arg(countryRowCountrySeqArg))(td)
    }, countryRowCountrySeqArg),
    unitField(name = "countriesByLastUpdate", desc = None, t = ListType(countryRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CountryRowService].getByLastUpdate(c.ctx.creds, c.arg(countryRowLastUpdateArg))(td)
    }, countryRowLastUpdateArg),
    unitField(name = "countriesByLastUpdateSeq", desc = None, t = ListType(countryRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CountryRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(countryRowLastUpdateSeqArg))(td)
    }, countryRowLastUpdateSeqArg)
  )

  val countryRowMutationType = ObjectType(
    name = "CountryRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(countryRowType), f = (c, td) => {
        c.ctx.getInstance[services.address.CountryRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(countryRowType), f = (c, td) => {
        c.ctx.getInstance[services.address.CountryRowService].update(c.ctx.creds, c.arg(countryRowCountryIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, countryRowCountryIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = countryRowType, f = (c, td) => {
        c.ctx.getInstance[services.address.CountryRowService].remove(c.ctx.creds, c.arg(countryRowCountryIdArg))(td)
      }, countryRowCountryIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "countryRow", desc = None, t = countryRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[CountryRow]) = {
    CountryRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
