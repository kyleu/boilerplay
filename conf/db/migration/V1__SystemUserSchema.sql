create extension if not exists hstore;

create table if not exists "system_users" (
  "id" uuid primary key,
  "username" character varying(256) unique,
  "provider" character varying(64) not null,
  "key" varchar(2048) not null,
  "prefs" character varying(4096) not null,
  "role" character varying(64) not null,
  "created" timestamp without time zone not null
);

create index if not exists "system_users_username" on "system_users" using btree ("username" asc);
create unique index if not exists "system_users_provider_key" on "system_users" using btree ("provider" asc, "key" asc);
create index if not exists "system_users_provider" on "system_users" using btree ("provider" asc);
create index if not exists "system_users_key" on "system_users" using btree ("key" asc);

create table if not exists "password_info" (
  "provider" varchar(64) not null,
  "key" varchar(2048) not null,
  "hasher" varchar(64) not null,
  "password" varchar(256) not null,
  "salt" varchar(256),
  "created" timestamp without time zone not null,
  constraint "password_info_pkey" primary key ("provider", "key")
);
create index if not exists "password_info_key" on "password_info" using btree ("key" asc);

create table if not exists "oauth2_info" (
  "provider" varchar(64) not null,
  "key" varchar(2048) not null,
  "access_token" varchar(2048) not null,
  "token_type" varchar(128),
  "expires_in" integer,
  "refresh_token" varchar(1024),
  "params" hstore,
  "created" timestamp without time zone not null,
  constraint "oauth2_info_pkey" primary key ("provider", "key")
);

create index if not exists "oauth2_info_key" on "oauth2_info" using btree ("key" asc);
