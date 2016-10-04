package models.ddl

case object CreatePasswordInfoTable extends CreateTableStatement("password_info") {
  override val sql = s"""
    create table "$tableName" (
       "provider" character varying(64) not null,
       "key" character varying(2048) not null,
       "hasher" character varying(64) not null,
       "password" character varying(256) not null,
       "salt" character varying(256),
       "created" timestamp not null,
       constraint "${tableName}_pkey" primary key ("provider", "key")
    );
  """
}
