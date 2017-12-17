create table if not exists "system_users" (
  "id" uuid primary key,
  "username" character varying(256) unique,
  "prefs" character varying(4096) not null,
  "email" character varying(1024) not null unique,
  "role" character varying(64) not null,
  "created" timestamp without time zone not null
);

create table if not exists "password_info" (
  "provider" varchar(64) not null,
  "key" varchar(2048) not null,
  "hasher" varchar(64) not null,
  "password" varchar(256) not null,
  "salt" varchar(256),
  "created" timestamp without time zone not null,
  constraint "password_info_pkey" primary key ("provider", "key")
);
