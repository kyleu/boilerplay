// Generated File
namespace java models.sync

include "../../common.thrift"
include "../../result.thrift"

struct SyncProgress {
  1: required string key;
  2: required string status;
  3: required string message;
  4: required common.LocalDateTime lastTime;
}

struct SyncProgressResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<SyncProgress> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
