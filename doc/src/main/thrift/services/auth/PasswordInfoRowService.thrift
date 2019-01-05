// Generated File
namespace java services.auth

include "../../common.thrift"
include "../../result.thrift"
include "../../models/auth/PasswordInfoRow.thrift"

service PasswordInfoRowService {

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<PasswordInfoRow.PasswordInfoRow> getAll(
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
  list<PasswordInfoRow.PasswordInfoRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<PasswordInfoRow.PasswordInfoRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  PasswordInfoRow.PasswordInfoRow insert(
    1: common.Credentials creds,
    2: required PasswordInfoRow.PasswordInfoRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<PasswordInfoRow.PasswordInfoRow> models
  )
}
