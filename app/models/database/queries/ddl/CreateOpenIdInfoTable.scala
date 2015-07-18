package models.database.queries.ddl

import models.database.Statement

case object CreateOpenIdInfoTable extends Statement {
  override val sql = """
    create table openid_info
    (
       provider character varying(64) not null,
       key text not null,
       id text not null,
       attributes text not null,
       created timestamp without time zone not null,
       constraint pk_openid_info primary key (provider, key)
    ) with (oids = false);
  """
}
