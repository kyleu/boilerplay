// Generated File
namespace java models.note

include "../../common.thrift"
include "../../result.thrift"

struct Note {
  1: required common.UUID id;
  2: optional string relType;
  3: optional string relPk;
  4: required string text;
  5: required common.UUID author;
  6: required common.LocalDateTime created;
}

struct NoteResult {
  1: required list<result.Filter> filters;
  2: required list<result.OrderBy> orderBys;
  3: required common.int totalCount;
  4: required result.PagingOptions paging;
  5: required list<Note> results;
  6: required common.int durationMs;
  7: required common.LocalDateTime occurred;
}
