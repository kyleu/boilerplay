package models.ddl

case object CreateSettingsTable extends CreateTableStatement("setting_values") {
  override val sql = s"""
    create table "$tableName" (
      "k" character varying(256) primary key,
      "v" character varying(4096) not null
    );
  """
}
