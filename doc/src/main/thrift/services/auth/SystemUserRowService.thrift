// Generated File
namespace java services.auth

include "../../common.thrift"
include "../../result.thrift"
include "../../models/auth/SystemUserRow.thrift"

service SystemUserRowService {
  SystemUserRow.SystemUserRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  SystemUserRow.SystemUserRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<SystemUserRow.SystemUserRow> getAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )

  common.int searchCount(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters
  )
  list<SystemUserRow.SystemUserRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<SystemUserRow.SystemUserRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  SystemUserRow.SystemUserRow insert(
    1: common.Credentials creds,
    2: required SystemUserRow.SystemUserRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<SystemUserRow.SystemUserRow> models
  )
  SystemUserRow.SystemUserRow create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  SystemUserRow.SystemUserRow remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  SystemUserRow.SystemUserRow update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
