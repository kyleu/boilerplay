package models.ddl

import models.database.{Row, SingleRowQuery, Statement}
import models.queries.BaseQueries

import util.DateUtils

object DdlQueries extends BaseQueries[DdlFile] {
  override protected val tableName = "ddl"
  override protected val columns = Seq("id", "name", "sql", "applied")

  def getAll = GetAll(orderBy = Some("id"))
  def insert(f: DdlFile) = Insert(f)

  case class DoesTableExist(tableName: String) extends SingleRowQuery[Boolean] {
    override val sql = "select count(*) as c from information_schema.tables WHERE (table_name = ? or table_name = ?);"
    override val values = tableName :: tableName.toUpperCase :: Nil
    override def map(row: Row) = row.as[Long]("c") > 0
  }

  case object CreateDdlTable extends Statement {
    override val sql = """create table "ddl" (
       "id" integer primary key,
       "name" varchar(128) not null,
       "sql" text not null,
       "applied" timestamp not null
    );"""
  }

  case class DdlStatement(override val sql: String) extends Statement

  override protected def fromRow(row: Row) = DdlFile(
    row.as[Int]("id"), row.as[String]("name"), row.as[String]("sql"), DateUtils.fromJoda(row.as[org.joda.time.LocalDateTime]("applied"))
  )
  override protected def toDataSeq(f: DdlFile) = Seq(
    f.id, f.name, f.sql, DateUtils.toJoda(f.applied)
  )
}
