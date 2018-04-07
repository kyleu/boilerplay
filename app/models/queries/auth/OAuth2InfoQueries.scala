package models.queries.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import models.database.DatabaseFieldType._
import models.database.{DatabaseField, Row, Statement}
import models.queries.BaseQueries

object OAuth2InfoQueries extends BaseQueries[OAuth2Info]("oauth2.info", "oauth2_info") {
  override val fields = Seq(
    DatabaseField("Provider", "provider", StringType),
    DatabaseField("Key", "key", StringType),
    DatabaseField(title = "Access Token", prop = "accessToken", col = "access_token", typ = StringType),
    DatabaseField(title = "Token Type", prop = "tokenType", col = "token_type", typ = StringType),
    DatabaseField(title = "Expires In", prop = "expiresIn", col = "expires_in", typ = IntegerType),
    DatabaseField(title = "Refresh Token", prop = "refreshToken", col = "refresh_token", typ = StringType),
    DatabaseField(title = "Parameters", prop = "params", col = "params", typ = TagsType),
    DatabaseField("Created", "created", TimestampType)
  )
  override protected val pkColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("key")

  def getByPrimaryKey(provider: String, key: String) = new GetByPrimaryKey(Seq(provider, key))
  def removeByPrimaryKey(provider: String, key: String) = new RemoveByPrimaryKey(Seq(provider, key))

  final case class CreateOAuth2Info(l: LoginInfo, i: OAuth2Info) extends Statement {
    override val name = s"$key.create.oauth2.info"
    override val sql = insertSql
    override val values = Seq(l.providerID, l.providerKey) ++ toDataSeq(i)
  }

  final case class UpdateOAuth2Info(l: LoginInfo, i: OAuth2Info) extends Statement {
    override val name = s"$key.update.oauth2.info"
    override val sql = s"""update ${quote(tableName)} set
      ${quote("access_token")} = ?, ${quote("token_type")} = ?, ${quote("expires_in")} = ?,
      ${quote("refresh_token")} = ?, ${quote("params")} = ?, ${quote("created")} = ?
      where ${quote("provider")} = ? and ${quote("key")} = ?
    """.trim
    override val values = toDataSeq(i) ++ Seq[Any](l.providerID, l.providerKey)
  }

  override protected def fromRow(row: Row) = OAuth2Info(
    accessToken = row.as[String]("access_token"),
    tokenType = row.asOpt[String]("token_type"),
    expiresIn = row.asOpt[Int]("expires_in"),
    refreshToken = row.asOpt[String]("refresh_token"),
    params = row.asOpt[Map[String, String]]("params")
  )

  override protected def toDataSeq(i: OAuth2Info) = {
    Seq[Any](i.accessToken, i.tokenType, i.expiresIn, i.refreshToken, i.params, util.DateUtils.now)
  }
}
