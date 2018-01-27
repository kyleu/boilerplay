// Generated File
namespace java models.audit

include "../../common.thrift"
include "../../result.thrift"

struct AuditRecord {
  1: required common.UUID id;
  2: required common.UUID auditId;
  3: required string t;
  4: required list<string> pk;
  5: required string changes;
}

struct AuditRecordResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<AuditRecord> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
