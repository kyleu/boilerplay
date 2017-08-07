package models.queries.user

import models.database.Row
import models.queries.BaseQueries
import models.user.PasswordInfo

object PasswordInfoQueries extends BaseQueries[PasswordInfo] {
  override protected val tableName = "password_info"
  override protected val columns = Seq("provider", "key", "hasher", "password", "salt", "created")
  override protected val idColumns = Seq("provider", "key")
  override protected val searchColumns = Seq("provider", "key")

  def getById(provider: String, key: String) = GetById(Seq(provider, key))
  def getByIdSeq(idSeq: Seq[(String, String)]) = new SeqQuery(
    additionalSql = " where " + idSeq.map(_ => "(\"provider\" = ? and \"key\" = ?)").mkString(" or "),
    values = idSeq.flatMap(_.productIterator.toSeq)
  )

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = GetAll(orderBy, limit, offset)

  def searchCount(q: String) = SearchCount(q)
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Search(q, orderBy, limit, offset)

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = SearchExact(q, orderBy, limit, offset)

  def insert(model: PasswordInfo) = Insert(model)

  override protected def fromRow(row: Row) = PasswordInfo(
    provider = row.as[String]("provider"),
    key = row.as[String]("key"),
    hasher = row.as[String]("hasher"),
    password = row.as[String]("password"),
    salt = row.asOpt[String]("salt"),
    created = fromJoda(row.as[org.joda.time.LocalDateTime]("created"))
  )
  override protected def toDataSeq(o: PasswordInfo) = Seq[Any](
    o.provider, o.key, o.hasher, o.password, o.salt, toJoda(o.created)
  )
}
