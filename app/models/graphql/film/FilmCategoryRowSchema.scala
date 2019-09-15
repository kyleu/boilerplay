/* Generated File */
package models.graphql.film

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.film.{FilmCategoryRow, FilmCategoryRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object FilmCategoryRowSchema extends GraphQLSchemaHelper("filmCategoryRow") {
  implicit val filmCategoryRowPrimaryKeyId: HasId[FilmCategoryRow, (Int, Int)] = HasId[FilmCategoryRow, (Int, Int)](x => (x.filmId, x.categoryId))
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[(Int, Int)]) = c.getInstance[services.film.FilmCategoryRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val filmCategoryRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val filmCategoryRowFilmIdArg = Argument("filmId", IntType)
  val filmCategoryRowFilmIdSeqArg = Argument("filmIds", ListInputType(IntType))
  val filmCategoryRowCategoryIdArg = Argument("categoryId", IntType)
  val filmCategoryRowCategoryIdSeqArg = Argument("categoryIds", ListInputType(IntType))

  val filmCategoryRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val filmCategoryRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  val filmCategoryRowByFilmIdRelation = Relation[FilmCategoryRow, Int]("byFilmId", x => Seq(x.filmId))
  val filmCategoryRowByFilmIdFetcher = Fetcher.rel[GraphQLContext, FilmCategoryRow, FilmCategoryRow, (Int, Int)](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.film.FilmCategoryRowService]).getByFilmIdSeq(c.creds, rels(filmCategoryRowByFilmIdRelation))(c.trace)
  )

  val filmCategoryRowByCategoryIdRelation = Relation[FilmCategoryRow, Int]("byCategoryId", x => Seq(x.categoryId))
  val filmCategoryRowByCategoryIdFetcher = Fetcher.rel[GraphQLContext, FilmCategoryRow, FilmCategoryRow, (Int, Int)](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.film.FilmCategoryRowService]).getByCategoryIdSeq(c.creds, rels(filmCategoryRowByCategoryIdRelation))(c.trace)
  )

  implicit lazy val filmCategoryRowType: sangria.schema.ObjectType[GraphQLContext, FilmCategoryRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "film",
        fieldType = FilmRowSchema.filmRowType,
        resolve = ctx => FilmRowSchema.filmRowByPrimaryKeyFetcher.defer(ctx.value.filmId)
      ),
      Field(
        name = "category",
        fieldType = CategoryRowSchema.categoryRowType,
        resolve = ctx => CategoryRowSchema.categoryRowByPrimaryKeyFetcher.defer(ctx.value.categoryId)
      )
    )
  )

  implicit lazy val filmCategoryRowResultType: sangria.schema.ObjectType[GraphQLContext, FilmCategoryRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "filmCategoryRow", desc = None, t = OptionType(filmCategoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmCategoryRowService].getByPrimaryKey(c.ctx.creds, c.arg(filmCategoryRowFilmIdArg), c.arg(filmCategoryRowCategoryIdArg))(td)
    }, filmCategoryRowFilmIdArg, filmCategoryRowCategoryIdArg),
    unitField(name = "filmCategoryRowSearch", desc = None, t = filmCategoryRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.film.FilmCategoryRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "filmCategoriesByLastUpdate", desc = None, t = ListType(filmCategoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmCategoryRowService].getByLastUpdate(c.ctx.creds, c.arg(filmCategoryRowLastUpdateArg))(td)
    }, filmCategoryRowLastUpdateArg),
    unitField(name = "filmCategoriesByLastUpdateSeq", desc = None, t = ListType(filmCategoryRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmCategoryRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(filmCategoryRowLastUpdateSeqArg))(td)
    }, filmCategoryRowLastUpdateSeqArg)
  )

  val filmCategoryRowMutationType = ObjectType(
    name = "FilmCategoryRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(filmCategoryRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.FilmCategoryRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(filmCategoryRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.FilmCategoryRowService].update(c.ctx.creds, c.arg(filmCategoryRowFilmIdArg), c.arg(filmCategoryRowCategoryIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, filmCategoryRowFilmIdArg, filmCategoryRowCategoryIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = filmCategoryRowType, f = (c, td) => {
        c.ctx.getInstance[services.film.FilmCategoryRowService].remove(c.ctx.creds, c.arg(filmCategoryRowFilmIdArg), c.arg(filmCategoryRowCategoryIdArg))(td)
      }, filmCategoryRowFilmIdArg, filmCategoryRowCategoryIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "filmCategoryRow", desc = None, t = filmCategoryRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[FilmCategoryRow]) = {
    FilmCategoryRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
