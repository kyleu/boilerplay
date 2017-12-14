create extension if not exists hstore;

create table if not exists "audit" (
  "id" uuid not null,
  "act" character varying(32) not null,
  "app" character varying(64) not null,
  "client" character varying(32) not null,
  "server" character varying(32) not null,
  "user_id" uuid not null,
  "tags" hstore not null,
  "msg" text not null,
  "started" timestamp without time zone not null,
  "completed" timestamp without time zone not null,
  primary key ("id")
) with (oids = false);

create index if not exists "audit_act" on "audit" using btree ("act" asc nulls last);
create index if not exists "audit_app" on "audit" using btree ("app" asc nulls last);
create index if not exists "audit_client" on "audit" using btree ("client" asc nulls last);
create index if not exists "audit_server" on "audit" using btree ("server" asc nulls last);
create index if not exists "audit_user_id" on "audit" using btree ("user_id" asc nulls last);
create index if not exists "audit_tags" on "audit" using gin ("tags");

create table if not exists "audit_record" (
  "id" uuid not null,
  "audit_id" uuid not null references "audit",
  "t" character varying(128) not null,
  "pk" character varying(128)[] not null,
  "changes" jsonb not null,
  primary key ("id")
) with (oids = false);

create index if not exists "audit_record_t" on "audit_record" using btree ("t" asc nulls last);
create index if not exists "audit_record_pk" on "audit_record" using btree ("pk" asc nulls last);
create index if not exists "audit_record_changes" on "audit_record" using gin ("changes");

create table if not exists "note" (
  "id" uuid primary key,
  "rel_type" varchar(128),
  "rel_pk" varchar(256),
  "text" text not null,
  "author" uuid not null,
  "created" timestamp without time zone not null,
  foreign key ("author") references "system_users" ("id")
);
