// Generated File
namespace java models.ddl

include "../../common.thrift"
include "../../result.thrift"

struct FlywaySchemaHistoryRow {
  1: required common.long installedRank;
  2: optional string version;
  3: required string description;
  4: required string typ;
  5: required string script;
  6: optional common.long checksum;
  7: required string installedBy;
  8: required common.LocalDateTime installedOn;
  9: required common.long executionTime;
  10: required bool success;
}

struct FlywaySchemaHistoryRowResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<FlywaySchemaHistoryRow> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
