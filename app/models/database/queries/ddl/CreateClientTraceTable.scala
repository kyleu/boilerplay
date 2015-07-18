package models.database.queries.ddl

import models.database.Statement

case object CreateClientTraceTable extends Statement {
  override val sql = """
    create table client_trace (
      id uuid not null primary key,
      player uuid not null,
      data json not null,
      created timestamp not null
    ) with (oids=false);

    alter table client_trace add constraint client_trace_users_fk foreign key (player) references users (id) on update no action on delete no action;
  """
}
