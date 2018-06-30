create type setting_key as enum('AllowRegistration', 'DefaultNewUserRole');
/* alter type "setting_key" add value 'NewValue' after 'OldValue'; */

create table if not exists "setting_values" (
  "k" setting_key primary key,
  "v" character varying(4096) not null
);
