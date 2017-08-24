package models.database

object DatabaseField {
  def apply(name: String) = new DatabaseField(name, name, name, DatabaseFieldType.StringType)
  def apply(name: String, typ: DatabaseFieldType[_]) = new DatabaseField(name, name, name, typ)
  def apply(title: String, name: String, typ: DatabaseFieldType[_]) = new DatabaseField(title, name, name, typ)
}

case class DatabaseField(title: String, prop: String, col: String, typ: DatabaseFieldType[_])
