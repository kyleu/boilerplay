package models.database.queries.ddl

import models.database.Statement

case object CreateOAuth1InfoTable extends Statement {
  override val sql = """
    create table oauth1_info
    (
       provider character varying(64) not null,
       key text not null,
       token text not null,
       secret text not null,
       created timestamp without time zone not null,
       constraint pk_oauth1_info primary key (provider, key)
    ) with (oids = false);
  """
}
