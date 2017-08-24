package models.ddl

import models.database.DatabaseFieldType._
import models.database._
import models.queries.BaseQueries

object DdlQueries extends BaseQueries[DdlFile]("ddl", "ddl") {
  override val fields = Seq(DatabaseField("id"), DatabaseField("name"), DatabaseField("sql"), DatabaseField("applied"))

  def getAll = GetAll
  case object GetIds extends Query[Seq[Int]] {
    override val name = "ddl.get.ids"
    override val sql = s"select ${quote("id")} from ${quote(tableName)} order by ${quote("id")}"
    override def reduce(rows: Iterator[Row]) = rows.map(_.as[Int]("id")).toSeq
  }
  def insert(f: DdlFile) = Insert(f)

  case class DoesTableExist(tableName: String) extends SingleRowQuery[Boolean] {
    override val name = "ddl.does.table.exist"
    override val sql = {
      s"select count(*) as c from ${quote("information_schema")}.${quote("tables")} WHERE (${quote("table_name")} = ? or ${quote("table_name")} = ?);"
    }
    override val values = tableName :: tableName.toUpperCase :: Nil
    override def map(row: Row) = row.as[Long]("c") > 0
  }

  case object CreateDdlTable extends Statement {
    override val name = "ddl.create.ddl.table"
    override val sql = s"""create table ${quote("ddl")} (
       ${quote("id")} integer primary key,
       ${quote("name")} varchar(128) not null,
       ${quote("sql")} text not null,
       ${quote("applied")} timestamp not null
    );"""
  }

  case class DdlStatement(override val sql: String, override val name: String = "ddl.adhoc") extends Statement

  override protected def fromRow(row: Row) = DdlFile(
    id = IntegerType(row, "id"),
    name = StringType(row, "name"),
    sql = StringType(row, "sql"),
    applied = TimestampType(row, "applied")
  )
}
