package models.queries.settings

import models.database.{Row, Statement}
import models.queries.BaseQueries
import models.settings.{Setting, SettingKey}

object SettingQueries extends BaseQueries[Setting] {
  override protected val tableName = "setting_values"
  override protected val idColumns = Seq("k")
  override protected val columns = Seq("k", "v")
  override protected val searchColumns = columns

  val insert = Insert
  def removeById(k: SettingKey) = RemoveById(Seq(k.toString))
  val getAll = GetAll()
  def getById(k: SettingKey) = GetById(Seq(k.toString))
  def search(q: String, orderBy: Option[String], limit: Option[Int] = None, offset: Option[Int] = None) = Search(q, orderBy, limit, offset)

  case class Update(s: Setting) extends Statement {
    override val sql = {
      updateSql(Seq("v"))
    }
    override val values = Seq[Any](s.value, s.key.toString)
  }

  override protected def fromRow(row: Row) = Setting(SettingKey.withName(row.as[String]("k")), row.as[String]("v"))
  override protected def toDataSeq(s: Setting) = Seq[Any](s.key.toString, s.value)
}
