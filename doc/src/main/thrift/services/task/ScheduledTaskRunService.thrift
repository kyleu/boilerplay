// Generated File
namespace java services.task

include "../../common.thrift"
include "../../result.thrift"
include "../../models/task/ScheduledTaskRun.thrift"

service ScheduledTaskRunService {
  ScheduledTaskRun.ScheduledTaskRun getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  ScheduledTaskRun.ScheduledTaskRun getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<ScheduledTaskRun.ScheduledTaskRun> getAll(
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
  list<ScheduledTaskRun.ScheduledTaskRun> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<ScheduledTaskRun.ScheduledTaskRun> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  ScheduledTaskRun.ScheduledTaskRun insert(
    1: common.Credentials creds,
    2: required ScheduledTaskRun.ScheduledTaskRun model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<ScheduledTaskRun.ScheduledTaskRun> models
  )
  ScheduledTaskRun.ScheduledTaskRun create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  ScheduledTaskRun.ScheduledTaskRun remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  ScheduledTaskRun.ScheduledTaskRun update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
