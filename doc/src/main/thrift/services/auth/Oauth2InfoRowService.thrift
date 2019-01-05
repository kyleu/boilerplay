// Generated File
namespace java services.auth

include "../../common.thrift"
include "../../result.thrift"
include "../../models/auth/Oauth2InfoRow.thrift"

service Oauth2InfoRowService {

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<Oauth2InfoRow.Oauth2InfoRow> getAll(
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
  list<Oauth2InfoRow.Oauth2InfoRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<Oauth2InfoRow.Oauth2InfoRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  Oauth2InfoRow.Oauth2InfoRow insert(
    1: common.Credentials creds,
    2: required Oauth2InfoRow.Oauth2InfoRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<Oauth2InfoRow.Oauth2InfoRow> models
  )
}
