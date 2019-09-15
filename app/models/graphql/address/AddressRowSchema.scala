/* Generated File */
package models.graphql.address

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.address.{AddressRow, AddressRowResult}
import models.graphql.customer.CustomerRowSchema
import models.graphql.store.{StaffRowSchema, StoreRowSchema}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object AddressRowSchema extends GraphQLSchemaHelper("addressRow") {
  implicit val addressRowPrimaryKeyId: HasId[AddressRow, Int] = HasId[AddressRow, Int](_.addressId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.address.AddressRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val addressRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val addressRowAddressIdArg = Argument("addressId", IntType)
  val addressRowAddressIdSeqArg = Argument("addressIds", ListInputType(IntType))

  val addressRowAddressArg = Argument("address", StringType)
  val addressRowAddressSeqArg = Argument("addresss", ListInputType(StringType))
  val addressRowAddress2Arg = Argument("address2", StringType)
  val addressRowAddress2SeqArg = Argument("address2s", ListInputType(StringType))
  val addressRowDistrictArg = Argument("district", StringType)
  val addressRowDistrictSeqArg = Argument("districts", ListInputType(StringType))
  val addressRowCityIdArg = Argument("cityId", IntType)
  val addressRowCityIdSeqArg = Argument("cityIds", ListInputType(IntType))
  val addressRowPostalCodeArg = Argument("postalCode", StringType)
  val addressRowPostalCodeSeqArg = Argument("postalCodes", ListInputType(StringType))
  val addressRowPhoneArg = Argument("phone", StringType)
  val addressRowPhoneSeqArg = Argument("phones", ListInputType(StringType))
  val addressRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val addressRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  val addressRowByCityIdRelation = Relation[AddressRow, Int]("byCityId", x => Seq(x.cityId))
  val addressRowByCityIdFetcher = Fetcher.rel[GraphQLContext, AddressRow, AddressRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.address.AddressRowService]).getByCityIdSeq(c.creds, rels(addressRowByCityIdRelation))(c.trace)
  )

  implicit lazy val addressRowType: sangria.schema.ObjectType[GraphQLContext, AddressRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "customers",
        fieldType = ListType(CustomerRowSchema.customerRowType),
        resolve = c => CustomerRowSchema.customerRowByAddressIdFetcher.deferRelSeq(
          CustomerRowSchema.customerRowByAddressIdRelation, c.value.addressId
        )
      ),
      Field(
        name = "staff",
        fieldType = ListType(StaffRowSchema.staffRowType),
        resolve = c => StaffRowSchema.staffRowByAddressIdFetcher.deferRelSeq(
          StaffRowSchema.staffRowByAddressIdRelation, c.value.addressId
        )
      ),
      Field(
        name = "stores",
        fieldType = ListType(StoreRowSchema.storeRowType),
        resolve = c => StoreRowSchema.storeRowByAddressIdFetcher.deferRelSeq(
          StoreRowSchema.storeRowByAddressIdRelation, c.value.addressId
        )
      ),
      Field(
        name = "city",
        fieldType = CityRowSchema.cityRowType,
        resolve = ctx => CityRowSchema.cityRowByPrimaryKeyFetcher.defer(ctx.value.cityId)
      )
    )
  )

  implicit lazy val addressRowResultType: sangria.schema.ObjectType[GraphQLContext, AddressRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "addressRow", desc = None, t = OptionType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByPrimaryKey(c.ctx.creds, c.arg(addressRowAddressIdArg))(td)
    }, addressRowAddressIdArg),
    unitField(name = "addressRowSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(addressRowAddressIdSeqArg))(td)
    }, addressRowAddressIdSeqArg),
    unitField(name = "addressRowSearch", desc = None, t = addressRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.address.AddressRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "addressesByAddress", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByAddress(c.ctx.creds, c.arg(addressRowAddressArg))(td)
    }, addressRowAddressArg),
    unitField(name = "addressesByAddressSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByAddressSeq(c.ctx.creds, c.arg(addressRowAddressSeqArg))(td)
    }, addressRowAddressSeqArg),
    unitField(name = "addressesByAddress2", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByAddress2(c.ctx.creds, c.arg(addressRowAddress2Arg))(td)
    }, addressRowAddress2Arg),
    unitField(name = "addressesByAddress2Seq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByAddress2Seq(c.ctx.creds, c.arg(addressRowAddress2SeqArg))(td)
    }, addressRowAddress2SeqArg),
    unitField(name = "addressesByDistrict", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByDistrict(c.ctx.creds, c.arg(addressRowDistrictArg))(td)
    }, addressRowDistrictArg),
    unitField(name = "addressesByDistrictSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByDistrictSeq(c.ctx.creds, c.arg(addressRowDistrictSeqArg))(td)
    }, addressRowDistrictSeqArg),
    unitField(name = "addressesByCityId", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByCityId(c.ctx.creds, c.arg(addressRowCityIdArg))(td)
    }, addressRowCityIdArg),
    unitField(name = "addressesByCityIdSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByCityIdSeq(c.ctx.creds, c.arg(addressRowCityIdSeqArg))(td)
    }, addressRowCityIdSeqArg),
    unitField(name = "addressesByPostalCode", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByPostalCode(c.ctx.creds, c.arg(addressRowPostalCodeArg))(td)
    }, addressRowPostalCodeArg),
    unitField(name = "addressesByPostalCodeSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByPostalCodeSeq(c.ctx.creds, c.arg(addressRowPostalCodeSeqArg))(td)
    }, addressRowPostalCodeSeqArg),
    unitField(name = "addressesByPhone", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByPhone(c.ctx.creds, c.arg(addressRowPhoneArg))(td)
    }, addressRowPhoneArg),
    unitField(name = "addressesByPhoneSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByPhoneSeq(c.ctx.creds, c.arg(addressRowPhoneSeqArg))(td)
    }, addressRowPhoneSeqArg),
    unitField(name = "addressesByLastUpdate", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByLastUpdate(c.ctx.creds, c.arg(addressRowLastUpdateArg))(td)
    }, addressRowLastUpdateArg),
    unitField(name = "addressesByLastUpdateSeq", desc = None, t = ListType(addressRowType), f = (c, td) => {
      c.ctx.getInstance[services.address.AddressRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(addressRowLastUpdateSeqArg))(td)
    }, addressRowLastUpdateSeqArg)
  )

  val addressRowMutationType = ObjectType(
    name = "AddressRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(addressRowType), f = (c, td) => {
        c.ctx.getInstance[services.address.AddressRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(addressRowType), f = (c, td) => {
        c.ctx.getInstance[services.address.AddressRowService].update(c.ctx.creds, c.arg(addressRowAddressIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, addressRowAddressIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = addressRowType, f = (c, td) => {
        c.ctx.getInstance[services.address.AddressRowService].remove(c.ctx.creds, c.arg(addressRowAddressIdArg))(td)
      }, addressRowAddressIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "addressRow", desc = None, t = addressRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[AddressRow]) = {
    AddressRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
