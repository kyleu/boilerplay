/* Generated File */
package models.graphql.film

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.film.{CategoryRow, CategoryRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object CategoryRowSchema extends GraphQLSchemaHelper("categoryRow") {
  implicit val categoryRowPrimaryKeyId: HasId[CategoryRow, Int] = HasId[CategoryRow, Int](_.categoryId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.film.CategoryRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val categoryRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val categoryRowCategoryIdArg = Argument("categoryId", IntType)
  val categoryRowCategoryIdSeqArg = Argument("categoryIds", ListInputType(IntType))

  val categoryRowNameArg = Argument("name", StringType)
  val categoryRowNameSeqArg = Argument("names", ListInputType(StringType))
  val categoryRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val categoryRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  implicit lazy val categoryRowType: sangria.schema.ObjectType[GraphQLContext, CategoryRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "filmCategoryCategoryIdFkey",
        fieldType = ListType(FilmCategoryRowSchema.filmCategoryRowType),
        resolve = c => FilmCategoryRowSchema.filmCategoryRowByCategoryIdFetcher.deferRelSeq(
          FilmCategoryRowSchema.filmCategoryRowByCategoryIdRelation, c.value.categoryId
        )
      )
    )
  )

  implicit lazy val categoryRowResultType: sangria.schema.ObjectType[GraphQLContext, CategoryRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "categoryRow", desc = None, t = OptionType(categoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.CategoryRowService].getByPrimaryKey(c.ctx.creds, c.arg(categoryRowCategoryIdArg))(td)
    }, categoryRowCategoryIdArg),
    unitField(name = "categoryRowSeq", desc = None, t = ListType(categoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.CategoryRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(categoryRowCategoryIdSeqArg))(td)
    }, categoryRowCategoryIdSeqArg),
    unitField(name = "categoryRowSearch", desc = None, t = categoryRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.film.CategoryRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "categoriesByName", desc = None, t = ListType(categoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.CategoryRowService].getByName(c.ctx.creds, c.arg(categoryRowNameArg))(td)
    }, categoryRowNameArg),
    unitField(name = "categoriesByNameSeq", desc = None, t = ListType(categoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.CategoryRowService].getByNameSeq(c.ctx.creds, c.arg(categoryRowNameSeqArg))(td)
    }, categoryRowNameSeqArg),
    unitField(name = "categoriesByLastUpdate", desc = None, t = ListType(categoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.CategoryRowService].getByLastUpdate(c.ctx.creds, c.arg(categoryRowLastUpdateArg))(td)
    }, categoryRowLastUpdateArg),
    unitField(name = "categoriesByLastUpdateSeq", desc = None, t = ListType(categoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.CategoryRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(categoryRowLastUpdateSeqArg))(td)
    }, categoryRowLastUpdateSeqArg)
  )

  val categoryRowMutationType = ObjectType(
    name = "CategoryRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(categoryRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.CategoryRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(categoryRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.CategoryRowService].update(c.ctx.creds, c.arg(categoryRowCategoryIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, categoryRowCategoryIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = categoryRowType, f = (c, td) => {
        c.ctx.getInstance[services.film.CategoryRowService].remove(c.ctx.creds, c.arg(categoryRowCategoryIdArg))(td)
      }, categoryRowCategoryIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "categoryRow", desc = None, t = categoryRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[CategoryRow]) = {
    CategoryRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
