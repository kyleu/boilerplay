package models.database.queries.ddl

import models.database.Statement

case object CreateDailyMetricsTable extends Statement {
  override val sql = """
    create table daily_metrics
    (
       day date not null,
       metric character varying(128) not null,
       value bigint not null default 0,
       measured timestamp without time zone not null,
       constraint pk_daily_metrics primary key (day, metric)
    ) with (oids = false);
  """
}
