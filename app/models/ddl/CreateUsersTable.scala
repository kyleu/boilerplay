package models.ddl

case object CreateUsersTable extends CreateTableStatement("users") {
  override val sql = s"""
    create table "$tableName" (
      "id" uuid primary key,
      "username" character varying(256),
      "prefs" character varying(4096) not null,
      "email" character varying(1024) not null,
      "role" character varying(64) not null,
      "created" timestamp not null
    );
  """
}
