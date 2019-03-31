/* Generated File */
package models.queries.auth

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import io.circe.Json
import java.time.LocalDateTime
import models.auth.Oauth2InfoRow

object Oauth2InfoRowQueries extends BaseQueries[Oauth2InfoRow]("oauth2InfoRow", "oauth2_info") {
  override val fields = Seq(
    DatabaseField(title = "Provider", prop = "provider", col = "provider", typ = StringType),
    DatabaseField(title = "Key", prop = "key", col = "key", typ = StringType),
    DatabaseField(title = "Access Token", prop = "accessToken", col = "access_token", typ = StringType),
    DatabaseField(title = "Token Type", prop = "tokenType", col = "token_type", typ = StringType),
    DatabaseField(title = "Expires In", prop = "expiresIn", col = "expires_in", typ = LongType),
    DatabaseField(title = "Refresh Token", prop = "refreshToken", col = "refresh_token", typ = StringType),
    DatabaseField(title = "Params", prop = "params", col = "params", typ = JsonType),
    DatabaseField(title = "Created", prop = "created", col = "created", typ = TimestampType)
  )
  override protected val pkColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("provider", "key")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(provider: String, key: String) = new GetByPrimaryKey(Seq[Any](provider, key))
  def getByPrimaryKeySeq(idSeq: Seq[(String, String)]) = new SeqQuery(
    whereClause = Some(idSeq.map(_ => "(\"provider\" = ? and \"key\" = ?)").mkString(" or ")),
    values = idSeq.flatMap(_.productIterator.toSeq)
  )

  final case class CountByKey(key: String) extends ColCount(column = "key", values = Seq(key))
  final case class GetByKey(key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("key") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(key)
  )
  final case class GetByKeySeq(keySeq: Seq[String]) extends ColSeqQuery(column = "key", values = keySeq)

  final case class CountByProvider(provider: String) extends ColCount(column = "provider", values = Seq(provider))
  final case class GetByProvider(provider: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("provider") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(provider)
  )
  final case class GetByProviderSeq(providerSeq: Seq[String]) extends ColSeqQuery(column = "provider", values = providerSeq)

  def insert(model: Oauth2InfoRow) = new Insert(model)
  def insertBatch(models: Seq[Oauth2InfoRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(provider: String, key: String) = new RemoveByPrimaryKey(Seq[Any](provider, key))

  def update(provider: String, key: String, fields: Seq[DataField]) = new UpdateFields(Seq[Any](provider, key), fields)

  override def fromRow(row: Row) = Oauth2InfoRow(
    provider = StringType(row, "provider"),
    key = StringType(row, "key"),
    accessToken = StringType(row, "access_token"),
    tokenType = StringType.opt(row, "token_type"),
    expiresIn = LongType.opt(row, "expires_in"),
    refreshToken = StringType.opt(row, "refresh_token"),
    params = JsonType.opt(row, "params"),
    created = TimestampType(row, "created")
  )
}
