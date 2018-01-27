// Generated File
namespace java services.user

include "../../common.thrift"
include "../../result.thrift"
include "../../models/user/SystemUser.thrift"

service SystemUserService {
  SystemUser.SystemUser getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  SystemUser.SystemUser getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<SystemUser.SystemUser> getAll(
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
  list<SystemUser.SystemUser> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<SystemUser.SystemUser> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  SystemUser.SystemUser insert(
    1: common.Credentials creds,
    2: required SystemUser.SystemUser model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<SystemUser.SystemUser> models
  )
  SystemUser.SystemUser create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  SystemUser.SystemUser remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  SystemUser.SystemUser update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
