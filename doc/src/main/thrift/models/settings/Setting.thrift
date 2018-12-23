// Generated File
namespace java models.settings

include "../../common.thrift"
include "../../result.thrift"

struct Setting {
  1: required SettingKeyType k;
  2: required string v;
}

struct SettingResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<Setting> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
