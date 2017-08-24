package models.queries.settings

import models.database.DatabaseFieldType.StringType
import models.database.{DatabaseField, Row, Statement}
import models.queries.BaseQueries
import models.settings.{Setting, SettingKey}

object SettingQueries extends BaseQueries[Setting]("settings", "setting_values") {
  override val fields = Seq(DatabaseField("k"), DatabaseField("v"))
  override protected val pkColumns = Seq("k")
  override protected val searchColumns = fields.map(_.col)

  val insert = Insert
  def removeByPrimaryKey(k: SettingKey) = RemoveByPrimaryKey(Seq(k.toString))
  val getAll = GetAll
  def getByPrimaryKey(k: SettingKey) = GetByPrimaryKey(Seq(k.toString))
  def search = Search

  case class Update(s: Setting) extends Statement {
    override val name = s"user.search.update"
    override val sql = updateSql(Seq("v"))
    override val values = Seq[Any](s.value, s.key.toString)
  }

  override protected def fromRow(row: Row) = Setting(SettingKey.withName(StringType(row, "k")), StringType(row, "v"))
  override protected def toDataSeq(s: Setting) = Seq[Any](s.key.toString, s.value)
}
