package models.database

object DatabaseField {
  def apply(name: String) = new DatabaseField(name, name, name, "string")
  def apply(name: String, typ: String) = new DatabaseField(name, name, name, typ)
  def apply(title: String, name: String, typ: String) = new DatabaseField(title, name, name, typ)
}

case class DatabaseField(title: String, prop: String, col: String, typ: String)
