package models.database.queries.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth2Info
import models.database.queries.BaseQueries
import models.database.{ Row, Statement }
import play.api.libs.json.{ JsValue, Json }
import utils.DateUtils

object OAuth2InfoQueries extends BaseQueries[OAuth2Info] {
  override protected val tableName = "oauth2_info"
  override protected val columns = Seq("provider", "key", "access_token", "token_type", "expires_in", "refresh_token", "params", "created")
  override protected val idColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("key", "access_token")

  val getById = GetById
  val removeById = RemoveById

  case class CreateOAuth2Info(l: LoginInfo, o: OAuth2Info) extends Statement {
    override val sql = insertSql
    override val values = Seq(l.providerID, l.providerKey) ++ toDataSeq(o)
  }

  case class UpdateOAuth2Info(l: LoginInfo, o: OAuth2Info) extends Statement {
    override val sql = {
      s"update $tableName set access_token = ?, token_type = ?, expires_in = ?, refresh_token = ?, params = ?, created = ? where provider = ? and key = ?"
    }
    override val values = toDataSeq(o) ++ Seq(l.providerID, l.providerKey)
  }

  override protected def fromRow(row: Row) = {
    val params = row.asOpt[String]("params").map { p =>
      Json.parse(p).as[Map[String, JsValue]].map(x => x._1 -> x._2.as[String])
    }
    OAuth2Info(
      accessToken = row.as[String]("access_token"),
      tokenType = row.asOpt[String]("token_type"),
      expiresIn = row.asOpt[Int]("expires_in"),
      refreshToken = row.asOpt[String]("refresh_token"),
      params = params
    )
  }

  override protected def toDataSeq(o: OAuth2Info) = {
    val params = o.params.map(p => Json.prettyPrint(Json.toJson(p)))
    Seq(o.accessToken, o.tokenType, o.expiresIn, o.refreshToken, params, DateUtils.now)
  }
}
