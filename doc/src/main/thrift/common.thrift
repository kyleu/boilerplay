namespace java common

typedef i32 int
typedef i64 long

struct LocalDateTime {
  1: required string v
}

struct Tag {
  1: required string k
  2: required string v
}

struct UUID {
  1: required string v
}

struct BigDecimal {
  1: required string v
}

struct DataField {
  1: required string k
  2: optional string v
}

struct Credentials {
  1: required UUID user;
  2: required string remoteAddress
  3: required map<string, string> tags;
}
