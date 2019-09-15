/* Generated File */
package models.queries.film

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.film.{FilmRow, MpaaRatingType}

object FilmRowQueries extends BaseQueries[FilmRow]("filmRow", "film") {
  override val fields = Seq(
    DatabaseField(title = "Film Id", prop = "filmId", col = "film_id", typ = IntegerType),
    DatabaseField(title = "Title", prop = "title", col = "title", typ = StringType),
    DatabaseField(title = "Description", prop = "description", col = "description", typ = StringType),
    DatabaseField(title = "Release Year", prop = "releaseYear", col = "release_year", typ = LongType),
    DatabaseField(title = "Language Id", prop = "languageId", col = "language_id", typ = IntegerType),
    DatabaseField(title = "Original Language Id", prop = "originalLanguageId", col = "original_language_id", typ = IntegerType),
    DatabaseField(title = "Rental Duration", prop = "rentalDuration", col = "rental_duration", typ = IntegerType),
    DatabaseField(title = "Rental Rate", prop = "rentalRate", col = "rental_rate", typ = BigDecimalType),
    DatabaseField(title = "Length", prop = "length", col = "length", typ = IntegerType),
    DatabaseField(title = "Replacement Cost", prop = "replacementCost", col = "replacement_cost", typ = BigDecimalType),
    DatabaseField(title = "Rating", prop = "rating", col = "rating", typ = EnumType(MpaaRatingType)),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType),
    DatabaseField(title = "Special Features", prop = "specialFeatures", col = "special_features", typ = StringArrayType),
    DatabaseField(title = "Fulltext", prop = "fulltext", col = "fulltext", typ = StringType)
  )
  override protected val pkColumns = Seq("film_id")
  override protected val searchColumns = Seq("film_id", "title", "description", "release_year", "language_id", "original_language_id", "rental_duration", "rental_rate", "length", "rating", "last_update", "fulltext")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(filmId: Int) = new GetByPrimaryKey(Seq(filmId))
  def getByPrimaryKeySeq(filmIdSeq: Seq[Int]) = new ColSeqQuery(column = "film_id", values = filmIdSeq)

  final case class CountByDescription(description: String) extends ColCount(column = "description", values = Seq(description))
  final case class GetByDescription(description: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("description") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(description)
  )
  final case class GetByDescriptionSeq(descriptionSeq: Seq[String]) extends ColSeqQuery(column = "description", values = descriptionSeq)

  final case class CountByFilmId(filmId: Int) extends ColCount(column = "film_id", values = Seq(filmId))
  final case class GetByFilmId(filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("film_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(filmId)
  )
  final case class GetByFilmIdSeq(filmIdSeq: Seq[Int]) extends ColSeqQuery(column = "film_id", values = filmIdSeq)

  final case class CountByFulltext(fulltext: String) extends ColCount(column = "fulltext", values = Seq(fulltext))
  final case class GetByFulltext(fulltext: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("fulltext") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(fulltext)
  )
  final case class GetByFulltextSeq(fulltextSeq: Seq[String]) extends ColSeqQuery(column = "fulltext", values = fulltextSeq)

  final case class CountByLanguageId(languageId: Int) extends ColCount(column = "language_id", values = Seq(languageId))
  final case class GetByLanguageId(languageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("language_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(languageId)
  )
  final case class GetByLanguageIdSeq(languageIdSeq: Seq[Int]) extends ColSeqQuery(column = "language_id", values = languageIdSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  final case class CountByLength(length: Int) extends ColCount(column = "length", values = Seq(length))
  final case class GetByLength(length: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("length") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(length)
  )
  final case class GetByLengthSeq(lengthSeq: Seq[Int]) extends ColSeqQuery(column = "length", values = lengthSeq)

  final case class CountByOriginalLanguageId(originalLanguageId: Int) extends ColCount(column = "original_language_id", values = Seq(originalLanguageId))
  final case class GetByOriginalLanguageId(originalLanguageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("original_language_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(originalLanguageId)
  )
  final case class GetByOriginalLanguageIdSeq(originalLanguageIdSeq: Seq[Int]) extends ColSeqQuery(column = "original_language_id", values = originalLanguageIdSeq)

  final case class CountByRating(rating: MpaaRatingType) extends ColCount(column = "rating", values = Seq(rating))
  final case class GetByRating(rating: MpaaRatingType, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rating") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(rating)
  )
  final case class GetByRatingSeq(ratingSeq: Seq[MpaaRatingType]) extends ColSeqQuery(column = "rating", values = ratingSeq)

  final case class CountByReleaseYear(releaseYear: Long) extends ColCount(column = "release_year", values = Seq(releaseYear))
  final case class GetByReleaseYear(releaseYear: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("release_year") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(releaseYear)
  )
  final case class GetByReleaseYearSeq(releaseYearSeq: Seq[Long]) extends ColSeqQuery(column = "release_year", values = releaseYearSeq)

  final case class CountByRentalDuration(rentalDuration: Int) extends ColCount(column = "rental_duration", values = Seq(rentalDuration))
  final case class GetByRentalDuration(rentalDuration: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rental_duration") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(rentalDuration)
  )
  final case class GetByRentalDurationSeq(rentalDurationSeq: Seq[Int]) extends ColSeqQuery(column = "rental_duration", values = rentalDurationSeq)

  final case class CountByRentalRate(rentalRate: BigDecimal) extends ColCount(column = "rental_rate", values = Seq(rentalRate))
  final case class GetByRentalRate(rentalRate: BigDecimal, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rental_rate") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(rentalRate)
  )
  final case class GetByRentalRateSeq(rentalRateSeq: Seq[BigDecimal]) extends ColSeqQuery(column = "rental_rate", values = rentalRateSeq)

  final case class CountByTitle(title: String) extends ColCount(column = "title", values = Seq(title))
  final case class GetByTitle(title: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("title") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(title)
  )
  final case class GetByTitleSeq(titleSeq: Seq[String]) extends ColSeqQuery(column = "title", values = titleSeq)

  def insert(model: FilmRow) = new Insert(model)
  def insertBatch(models: Seq[FilmRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(filmId: Int) = new RemoveByPrimaryKey(Seq[Any](filmId))

  def update(filmId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](filmId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = FilmRow(
    filmId = IntegerType(row, "film_id"),
    title = StringType(row, "title"),
    description = StringType.opt(row, "description"),
    releaseYear = LongType.opt(row, "release_year"),
    languageId = IntegerType(row, "language_id"),
    originalLanguageId = IntegerType.opt(row, "original_language_id"),
    rentalDuration = IntegerType(row, "rental_duration"),
    rentalRate = BigDecimalType(row, "rental_rate"),
    length = IntegerType.opt(row, "length"),
    replacementCost = BigDecimalType(row, "replacement_cost"),
    rating = EnumType(MpaaRatingType).opt(row, "rating"),
    lastUpdate = TimestampZonedType(row, "last_update"),
    specialFeatures = StringArrayType.opt(row, "special_features"),
    fulltext = StringType(row, "fulltext")
  )
}
