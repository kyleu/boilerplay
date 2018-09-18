do $$
begin
  if not exists (select 1 from "pg_type" where "typname" = 'setting_key') then
    create type "setting_key" as enum('AllowRegistration', 'DefaultNewUserRole');
  end if;
end$$;

create table if not exists "setting_values" (
  "k" setting_key primary key,
  "v" character varying(4096) not null
);
