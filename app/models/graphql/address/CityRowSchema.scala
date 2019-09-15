/* Generated File */
package models.graphql.address

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.address.{CityRow, CityRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object CityRowSchema extends GraphQLSchemaHelper("cityRow") {
  implicit val cityRowPrimaryKeyId: HasId[CityRow, Int] = HasId[CityRow, Int](_.cityId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.address.CityRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val cityRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val cityRowCityIdArg = Argument("cityId", IntType)
  val cityRowCityIdSeqArg = Argument("cityIds", ListInputType(IntType))

  val cityRowCityArg = Argument("city", StringType)
  val cityRowCitySeqArg = Argument("citys", ListInputType(StringType))
  val cityRowCountryIdArg = Argument("countryId", IntType)
  val cityRowCountryIdSeqArg = Argument("countryIds", ListInputType(IntType))
  val cityRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val cityRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  val cityRowByCountryIdRelation = Relation[CityRow, Int]("byCountryId", x => Seq(x.countryId))
  val cityRowByCountryIdFetcher = Fetcher.rel[GraphQLContext, CityRow, CityRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.address.CityRowService]).getByCountryIdSeq(c.creds, rels(cityRowByCountryIdRelation))(c.trace)
  )

  implicit lazy val cityRowType: sangria.schema.ObjectType[GraphQLContext, CityRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "addresses",
        fieldType = ListType(AddressRowSchema.addressRowType),
        resolve = c => AddressRowSchema.addressRowByCityIdFetcher.deferRelSeq(
          AddressRowSchema.addressRowByCityIdRelation, c.value.cityId
        )
      ),
      Field(
        name = "country",
        fieldType = CountryRowSchema.countryRowType,
        resolve = ctx => CountryRowSchema.countryRowByPrimaryKeyFetcher.defer(ctx.value.countryId)
      )
    )
  )

  implicit lazy val cityRowResultType: sangria.schema.ObjectType[GraphQLContext, CityRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "cityRow", desc = None, t = OptionType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByPrimaryKey(c.ctx.creds, c.arg(cityRowCityIdArg))(td)
    }, cityRowCityIdArg),
    unitField(name = "cityRowSeq", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(cityRowCityIdSeqArg))(td)
    }, cityRowCityIdSeqArg),
    unitField(name = "cityRowSearch", desc = None, t = cityRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.address.CityRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "citiesByCity", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByCity(c.ctx.creds, c.arg(cityRowCityArg))(td)
    }, cityRowCityArg),
    unitField(name = "citiesByCitySeq", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByCitySeq(c.ctx.creds, c.arg(cityRowCitySeqArg))(td)
    }, cityRowCitySeqArg),
    unitField(name = "citiesByCountryId", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByCountryId(c.ctx.creds, c.arg(cityRowCountryIdArg))(td)
    }, cityRowCountryIdArg),
    unitField(name = "citiesByCountryIdSeq", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByCountryIdSeq(c.ctx.creds, c.arg(cityRowCountryIdSeqArg))(td)
    }, cityRowCountryIdSeqArg),
    unitField(name = "citiesByLastUpdate", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByLastUpdate(c.ctx.creds, c.arg(cityRowLastUpdateArg))(td)
    }, cityRowLastUpdateArg),
    unitField(name = "citiesByLastUpdateSeq", desc = None, t = ListType(cityRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.CityRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(cityRowLastUpdateSeqArg))(td)
    }, cityRowLastUpdateSeqArg)
  )

  val cityRowMutationType = ObjectType(
    name = "CityRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(cityRowType), f = (c, td) => {
        c.ctx.getInstance[services.address.CityRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(cityRowType), f = (c, td) => {
        c.ctx.getInstance[services.address.CityRowService].update(c.ctx.creds, c.arg(cityRowCityIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, cityRowCityIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = cityRowType, f = (c, td) => {
        c.ctx.getInstance[services.address.CityRowService].remove(c.ctx.creds, c.arg(cityRowCityIdArg))(td)
      }, cityRowCityIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "cityRow", desc = None, t = cityRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[CityRow]) = {
    CityRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
