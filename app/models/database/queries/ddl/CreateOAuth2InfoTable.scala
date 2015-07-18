package models.database.queries.ddl

import models.database.Statement

case object CreateOAuth2InfoTable extends Statement {
  override val sql = """
    create table oauth2_info
    (
       provider character varying(64) not null,
       key text not null,
       access_token text not null,
       token_type character varying(64),
       expires_in integer,
       refresh_token character varying(64),
       params text,
       created timestamp without time zone,
       constraint pk_oauth2_info primary key (provider, key)
    ) with (oids = false);
  """
}
