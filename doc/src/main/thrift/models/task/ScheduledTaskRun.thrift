// Generated File
namespace java models.task

include "../../common.thrift"
include "../../result.thrift"

struct ScheduledTaskRun {
  1: required common.UUID id;
  2: required string task;
  3: required list<string> arguments;
  4: required string status;
  5: required string output;
  6: required common.LocalDateTime started;
  7: required common.LocalDateTime completed;
}

struct ScheduledTaskRunResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<ScheduledTaskRun> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
