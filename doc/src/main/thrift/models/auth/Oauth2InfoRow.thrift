// Generated File
namespace java models.auth

include "../../common.thrift"
include "../../result.thrift"

struct Oauth2InfoRow {
  1: required string provider;
  2: required string key;
  3: required string accessToken;
  4: optional string tokenType;
  5: optional common.long expiresIn;
  6: optional string refreshToken;
  7: optional list<common.Tag> params;
  8: required common.LocalDateTime created;
}

struct Oauth2InfoRowResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<Oauth2InfoRow> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
