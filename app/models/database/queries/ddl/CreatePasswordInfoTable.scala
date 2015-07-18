package models.database.queries.ddl

import models.database.Statement

case object CreatePasswordInfoTable extends Statement {
  override val sql = """
    create table password_info
    (
       provider character varying(64) not null,
       key text not null,
       hasher character varying(64) not null,
       password character varying(256) not null,
       salt character varying(256),
       created timestamp without time zone not null,
       constraint pk_password_info primary key (provider, key)
    ) with (oids = false);
  """
}
