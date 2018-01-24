namespace java models.audit

include "../common.thrift"
include "../result.thrift"

struct Audit {
  1: required common.UUID id;
  2: required string act;
  3: required string app;
  4: required string client;
  5: required string server;
  6: required common.UUID userId;
  7: required common.Tag tags;
  8: required string msg;
  9: required list<AuditRecord> records;
  10: required common.LocalDateTime started
  11: required common.LocalDateTime completed
}

struct AuditField {
  1: required string k;
  2: optional string o;
  3: optional string n;
}

struct AuditRecord {
  1: required common.UUID id;
  2: required common.UUID auditId;
  3: required string t;
  4: required list<string> pk;
  5: required list<AuditField> changes;
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
