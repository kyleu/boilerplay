create table if not exists "scheduled_task_run" (
  "id" uuid not null primary key,
  "task" varchar(64) not null,
  "arguments" varchar(64)[] not null,
  "status" varchar(64) not null,
  "output" json not null,
  "started" timestamp without time zone not null,
  "completed" timestamp without time zone not null
);

create index if not exists "scheduled_task_run_task" on "scheduled_task_run" using btree ("task" asc);
create index if not exists "scheduled_task_run_status" on "scheduled_task_run" using btree ("status" asc);
create index if not exists "scheduled_task_run_started" on "scheduled_task_run" using btree ("started" asc);

create table if not exists "sync_progress" (
  "key" varchar(128) not null primary key,
  "status" varchar(128) not null,
  "message" text not null,
  "last_time" timestamp not null
);

create index if not exists "sync_progress_status" on "sync_progress" using btree ("status" asc);
