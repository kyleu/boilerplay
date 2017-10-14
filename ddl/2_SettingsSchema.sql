create table if not exists "setting_values" (
  "k" character varying(256) primary key,
  "v" character varying(4096) not null
);
