package models.database.queries.ddl

import models.database.Statement

case object CreateUsersTable extends Statement {
  override val sql = """
    create table users (
      id uuid primary key,
      username character varying(256),
      prefs json NOT NULL,
      profiles text[] not null,
      roles character varying(64)[] not null,
      created timestamp not null
    ) with (oids=false);

    create index users_profiles_idx on users using gin (profiles);
    create unique index users_username_idx on users using btree (username collate pg_catalog."default");
    create index users_roles_idx on users using gin (roles);
  """
}
