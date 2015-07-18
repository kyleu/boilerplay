package models.database.queries.ddl

import models.database.Statement

case object CreateSessionInfoTable extends Statement {
  override val sql = """
    create table session_info
    (
      id text not null,
      provider character varying(64) not null,
      key text not null,
      last_used timestamp without time zone not null,
      expiration timestamp without time zone not null,
      fingerprint text,
      created timestamp without time zone not null,
      constraint pk_session_info primary key (id)
    ) with (oids = false);

    create index idx_session_info_provider_key on session_info (provider, key);
  """
}
