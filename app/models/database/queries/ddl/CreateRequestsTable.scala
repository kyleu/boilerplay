package models.database.queries.ddl

import models.database.Statement

case object CreateRequestsTable extends Statement {
  override val sql = """
    create table requests (
      id uuid primary key not null,
      user_id uuid not null,
      auth_provider character varying(64) not null,
      auth_key text not null,
      remote_address character varying(64) not null,

      method character varying(10) not null,
      host text not null,
      secure boolean not null,
      path text not null,
      query_string text,

      lang text,
      cookie text,
      referrer text,
      user_agent text,
      started timestamp not null,
      duration integer not null,
      status integer not null
    ) with (oids=false);

    create index requests_account_idx on requests using btree (user_id);

    alter table requests add constraint requests_users_fk foreign key (user_id) references users (id) on update no action on delete no action;
  """
}
