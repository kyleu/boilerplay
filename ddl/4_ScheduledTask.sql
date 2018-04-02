create table "scheduled_task_run" (
  "id" uuid not null primary key,
  "task" varchar(64) not null,
  "arguments" varchar(64)[] not null,
  "status" varchar(64) not null,
  "output" json not null,
  "started" timestamp without time zone not null,
  "completed" timestamp without time zone not null
);

create table "sync_progress" (
  "key" varchar(128) not null primary key,
  "status" varchar(128) not null,
  "message" text not null,
  "last_time" timestamp not null
);
