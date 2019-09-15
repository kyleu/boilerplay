/* Generated File */
package models.graphql.store

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.graphql.customer.RentalRowSchema
import models.graphql.film.FilmRowSchema
import models.store.{InventoryRow, InventoryRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object InventoryRowSchema extends GraphQLSchemaHelper("inventoryRow") {
  implicit val inventoryRowPrimaryKeyId: HasId[InventoryRow, Long] = HasId[InventoryRow, Long](_.inventoryId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Long]) = c.getInstance[services.store.InventoryRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val inventoryRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val inventoryRowInventoryIdArg = Argument("inventoryId", LongType)
  val inventoryRowInventoryIdSeqArg = Argument("inventoryIds", ListInputType(LongType))

  val inventoryRowFilmIdArg = Argument("filmId", IntType)
  val inventoryRowFilmIdSeqArg = Argument("filmIds", ListInputType(IntType))
  val inventoryRowStoreIdArg = Argument("storeId", IntType)
  val inventoryRowStoreIdSeqArg = Argument("storeIds", ListInputType(IntType))

  val inventoryRowByFilmIdRelation = Relation[InventoryRow, Int]("byFilmId", x => Seq(x.filmId))
  val inventoryRowByFilmIdFetcher = Fetcher.rel[GraphQLContext, InventoryRow, InventoryRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.store.InventoryRowService]).getByFilmIdSeq(c.creds, rels(inventoryRowByFilmIdRelation))(c.trace)
  )

  val inventoryRowByStoreIdRelation = Relation[InventoryRow, Int]("byStoreId", x => Seq(x.storeId))
  val inventoryRowByStoreIdFetcher = Fetcher.rel[GraphQLContext, InventoryRow, InventoryRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.store.InventoryRowService]).getByStoreIdSeq(c.creds, rels(inventoryRowByStoreIdRelation))(c.trace)
  )

  implicit lazy val inventoryRowType: sangria.schema.ObjectType[GraphQLContext, InventoryRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "rentals",
        fieldType = ListType(RentalRowSchema.rentalRowType),
        resolve = c => RentalRowSchema.rentalRowByInventoryIdFetcher.deferRelSeq(
          RentalRowSchema.rentalRowByInventoryIdRelation, c.value.inventoryId
        )
      ),
      Field(
        name = "film",
        fieldType = FilmRowSchema.filmRowType,
        resolve = ctx => FilmRowSchema.filmRowByPrimaryKeyFetcher.defer(ctx.value.filmId)
      ),
      Field(
        name = "store",
        fieldType = StoreRowSchema.storeRowType,
        resolve = ctx => StoreRowSchema.storeRowByPrimaryKeyFetcher.defer(ctx.value.storeId)
      )
    )
  )

  implicit lazy val inventoryRowResultType: sangria.schema.ObjectType[GraphQLContext, InventoryRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "inventoryRow", desc = None, t = OptionType(inventoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.InventoryRowService].getByPrimaryKey(c.ctx.creds, c.arg(inventoryRowInventoryIdArg))(td)
    }, inventoryRowInventoryIdArg),
    unitField(name = "inventoryRowSeq", desc = None, t = ListType(inventoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.InventoryRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(inventoryRowInventoryIdSeqArg))(td)
    }, inventoryRowInventoryIdSeqArg),
    unitField(name = "inventoryRowSearch", desc = None, t = inventoryRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.store.InventoryRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "inventoriesByFilmId", desc = None, t = ListType(inventoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.InventoryRowService].getByFilmId(c.ctx.creds, c.arg(inventoryRowFilmIdArg))(td)
    }, inventoryRowFilmIdArg),
    unitField(name = "inventoriesByFilmIdSeq", desc = None, t = ListType(inventoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.InventoryRowService].getByFilmIdSeq(c.ctx.creds, c.arg(inventoryRowFilmIdSeqArg))(td)
    }, inventoryRowFilmIdSeqArg),
    unitField(name = "inventoriesByStoreId", desc = None, t = ListType(inventoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.InventoryRowService].getByStoreId(c.ctx.creds, c.arg(inventoryRowStoreIdArg))(td)
    }, inventoryRowStoreIdArg),
    unitField(name = "inventoriesByStoreIdSeq", desc = None, t = ListType(inventoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.InventoryRowService].getByStoreIdSeq(c.ctx.creds, c.arg(inventoryRowStoreIdSeqArg))(td)
    }, inventoryRowStoreIdSeqArg)
  )

  val inventoryRowMutationType = ObjectType(
    name = "InventoryRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(inventoryRowType), f = (c, td) => {
        c.ctx.getInstance[services.store.InventoryRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(inventoryRowType), f = (c, td) => {
        c.ctx.getInstance[services.store.InventoryRowService].update(c.ctx.creds, c.arg(inventoryRowInventoryIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, inventoryRowInventoryIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = inventoryRowType, f = (c, td) => {
        c.ctx.getInstance[services.store.InventoryRowService].remove(c.ctx.creds, c.arg(inventoryRowInventoryIdArg))(td)
      }, inventoryRowInventoryIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "inventoryRow", desc = None, t = inventoryRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[InventoryRow]) = {
    InventoryRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
