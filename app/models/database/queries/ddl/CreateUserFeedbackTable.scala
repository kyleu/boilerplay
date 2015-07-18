package models.database.queries.ddl

import models.database.Statement

object CreateUserFeedbackTable extends Statement {
  override def sql: String = """
    create table user_feedback (
      id uuid not null primary key,
      user_id uuid not null,
      feedback text not null,
      occurred timestamp without time zone not null
    ) with (oids=false);

    alter table user_feedback add constraint user_feedback_users_fk foreign key (user_id) references users (id) on update no action on delete no action;
    create index user_feedback_users_idx on user_feedback using btree (user_id);
  """
}
