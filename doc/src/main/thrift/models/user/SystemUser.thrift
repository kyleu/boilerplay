// Generated File
namespace java models.user

include "../../common.thrift"
include "../../result.thrift"

struct SystemUser {
  1: required common.UUID id;
  2: optional string username;
  3: required string provider;
  4: required string key;
  5: required string prefs;
  6: required string role;
  7: required common.LocalDateTime created;
}

struct SystemUserResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<SystemUser> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
