namespace java common.result

include "common.thrift"

struct Filter {
  1: required string k
  2: required string o
  3: required list<string> v
}

struct OrderBy {
  1: required string col
  2: required string dir
}

struct Range {
  1: required common.int startVal
  2: required common.int endVal
}

struct PagingOptions {
  1: required common.int current
  2: optional common.int limit
  3: optional common.int nextVal
  4: optional common.int previous
  5: required common.int pages
  6: required common.int itemsPerPage
  7: required Range range
  8: required common.int total
}
