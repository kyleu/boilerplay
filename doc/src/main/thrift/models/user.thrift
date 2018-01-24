namespace java models.user

include "../common.thrift"
include "../result.thrift"

struct LoginInfo {
  1: required string providerID
  2: required string providerKey
}

struct UserPreferences {
  1: required string theme
}

struct SystemUser {
  1: required common.UUID id;
  2: required string username;
  3: required UserPreferences preferences;
  4: required LoginInfo profile;
  5: required string role;
  6: required common.LocalDateTime created;
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
