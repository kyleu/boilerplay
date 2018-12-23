// Generated File
namespace java services.task

include "../../common.thrift"
include "../../result.thrift"
include "../../models/task/ScheduledTaskRunRow.thrift"

service ScheduledTaskRunRowService {
  ScheduledTaskRunRow.ScheduledTaskRunRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  ScheduledTaskRunRow.ScheduledTaskRunRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<ScheduledTaskRunRow.ScheduledTaskRunRow> getAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )

  common.int searchCount(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters
  )
  list<ScheduledTaskRunRow.ScheduledTaskRunRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<ScheduledTaskRunRow.ScheduledTaskRunRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  ScheduledTaskRunRow.ScheduledTaskRunRow insert(
    1: common.Credentials creds,
    2: required ScheduledTaskRunRow.ScheduledTaskRunRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<ScheduledTaskRunRow.ScheduledTaskRunRow> models
  )
  ScheduledTaskRunRow.ScheduledTaskRunRow create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  ScheduledTaskRunRow.ScheduledTaskRunRow remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  ScheduledTaskRunRow.ScheduledTaskRunRow update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
