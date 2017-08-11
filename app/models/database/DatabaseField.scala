package models.database

object DatabaseField {
  def apply(name: String, typ: String = "string") = new DatabaseField(name, name, typ)
}

case class DatabaseField(prop: String, col: String, typ: String)
