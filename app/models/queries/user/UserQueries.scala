package models.queries.user

import java.util.UUID
import models.database.Row
import models.queries.BaseQueries
import models.user.User

object UserQueries extends BaseQueries[User] {
  override protected val tableName = "users"
  override protected val columns = Seq("id", "username", "prefs", "email", "role", "created")
  override protected val idColumns = Seq("id")
  override protected val searchColumns = Seq("id")

  def getById(id: UUID) = GetById(Seq(id))
  def getByIdSeq(idSeq: Seq[UUID]) = new ColSeqQuery("id", idSeq)

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = GetAll(orderBy, limit, offset)

  def searchCount(q: String) = SearchCount(q)
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Search(q, orderBy, limit, offset)

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = SearchExact(q, orderBy, limit, offset)

  def insert(model: User) = Insert(model)
  def removeById(id: UUID) = RemoveById(Seq(id))

  override protected def fromRow(row: Row) = User(
    id = row.as[UUID]("id"),
    username = row.as[String]("username"),
    prefs = row.as[String]("prefs"),
    email = row.as[String]("email"),
    role = row.as[String]("role"),
    created = fromJoda(row.as[org.joda.time.LocalDateTime]("created"))
  )
  override protected def toDataSeq(o: User) = Seq[Any](
    o.id, o.username, o.prefs, o.email, o.role, toJoda(o.created)
  )
}
