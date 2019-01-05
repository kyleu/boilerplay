// Generated File
namespace java models.auth

include "../../common.thrift"
include "../../result.thrift"

struct PasswordInfoRow {
  1: required string provider;
  2: required string key;
  3: required string hasher;
  4: required string password;
  5: optional string salt;
  6: required common.LocalDateTime created;
}

struct PasswordInfoRowResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<PasswordInfoRow> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
