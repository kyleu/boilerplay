package models.database.queries.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OpenIDInfo
import models.database.queries.BaseQueries
import models.database.{ Row, Statement }
import play.api.libs.json.{ JsValue, Json }
import utils.DateUtils

object OpenIdInfoQueries extends BaseQueries[OpenIDInfo] {
  override protected val tableName = "openid_info"
  override protected val columns = Seq("provider", "key", "id", "attributes", "created")
  override protected val idColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("key")

  val getById = GetById
  val removeById = RemoveById

  case class CreateOpenIdInfo(l: LoginInfo, o: OpenIDInfo) extends Statement {
    override val sql = insertSql
    override val values = Seq(l.providerID, l.providerKey) ++ toDataSeq(o)
  }

  case class UpdateOpenIdInfo(l: LoginInfo, o: OpenIDInfo) extends Statement {
    override val sql = s"update $tableName set id = ?, attributes = ?, created = ? where provider = ? and key = ?"
    val attributes = Json.prettyPrint(Json.toJson(o.attributes))
    override val values = toDataSeq(o) ++ Seq(l.providerID, l.providerKey)
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[String]("id")
    val attributesString = row.as[String]("attributes")
    val attributes = Json.parse(attributesString).as[Map[String, JsValue]].map(x => x._1 -> x._2.as[String])
    OpenIDInfo(id, attributes)
  }

  override protected def toDataSeq(o: OpenIDInfo) = {
    val attributes = Json.prettyPrint(Json.toJson(o.attributes))
    Seq(o.id, attributes, DateUtils.now)
  }
}
