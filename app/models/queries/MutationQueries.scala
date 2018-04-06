package models.queries

import models.database.Statement
import models.result.ResultFieldHelper
import models.result.data.DataField

trait MutationQueries[T <: Product] { this: BaseQueries[T] =>
  protected def pkWhereClause = pkColumns.map(x => quote(x) + " = ?").mkString(" and ")

  protected class Insert(model: T) extends Statement {
    override val name = s"$key.insert"
    override val sql = insertSql
    override val values: Seq[Any] = toDataSeq(model)
  }

  protected class InsertFields(dataFields: Seq[DataField]) extends Statement {
    override val name = s"$key.insert.fields"
    private[this] val cols = dataFields.map(f => ResultFieldHelper.sqlForField("update", f.k, fields))
    override val sql = s"insert into ${quote(tableName)} (${cols.mkString(", ")}) values (${cols.map(_ => "?").mkString(", ")})"
    override val values: Seq[Any] = dataFields.map(_.v)
  }

  protected class InsertBatch(models: Seq[T]) extends Statement {
    override val name = s"$key.insert.batch"
    private[this] val valuesClause = models.map(_ => s"($columnPlaceholders)").mkString(", ")
    override val sql = s"""insert into ${quote(tableName)} ($quotedColumns) values $valuesClause"""
    override val values: Seq[Any] = models.flatMap(toDataSeq)
  }

  protected class RemoveByPrimaryKey(override val values: Seq[Any]) extends Statement {
    override val name = s"$key.remove.by.primary.key"
    override val sql = s"""delete from ${quote(tableName)} where $pkWhereClause"""
  }

  protected class CreateFields(dataFields: Seq[DataField]) extends Statement {
    override val name = s"$key.create.fields"
    private[this] val cols = dataFields.map(f => ResultFieldHelper.sqlForField("insert", f.k, fields))
    override val sql = s"""insert into ${quote(tableName)} (${cols.mkString(", ")}) values (${cols.map(_ => "?").mkString(", ")})"""
    override val values = dataFields.map(f => ResultFieldHelper.valueForField("create", f.k, f.v, fields))
  }

  protected class UpdateFields(pks: Seq[Any], dataFields: Seq[DataField]) extends Statement {
    override val name = s"$key.update.fields"
    private[this] val cols = dataFields.map(f => ResultFieldHelper.sqlForField("update", f.k, fields))
    override val sql = s"""update ${quote(tableName)} set ${cols.map(_ + " = ?").mkString(", ")} where $pkWhereClause"""
    override val values = dataFields.map(f => ResultFieldHelper.valueForField("update", f.k, f.v, fields)) ++ pks
  }
}
