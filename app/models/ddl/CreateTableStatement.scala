package models.ddl

import models.database.Statement

abstract class CreateTableStatement(val tableName: String) extends Statement {

}
