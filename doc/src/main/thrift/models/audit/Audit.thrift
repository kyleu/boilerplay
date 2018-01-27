// Generated File
namespace java models.audit

include "../../common.thrift"
include "../../result.thrift"

struct Audit {
  1: required common.UUID id;
  2: required string act;
  3: required string app;
  4: required string client;
  5: required string server;
  6: required common.UUID userId;
  7: required list<common.Tag> tags;
  8: required string msg;
  9: required common.LocalDateTime started;
  10: required common.LocalDateTime completed;
}

struct AuditResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<Audit> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
