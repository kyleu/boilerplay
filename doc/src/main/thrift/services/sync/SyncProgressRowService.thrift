// Generated File
namespace java services.sync

include "../../common.thrift"
include "../../result.thrift"
include "../../models/sync/SyncProgressRow.thrift"

service SyncProgressRowService {
  SyncProgressRow.SyncProgressRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required string key
  )
  SyncProgressRow.SyncProgressRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<string> key
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<SyncProgressRow.SyncProgressRow> getAll(
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
  list<SyncProgressRow.SyncProgressRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<SyncProgressRow.SyncProgressRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  SyncProgressRow.SyncProgressRow insert(
    1: common.Credentials creds,
    2: required SyncProgressRow.SyncProgressRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<SyncProgressRow.SyncProgressRow> models
  )
  SyncProgressRow.SyncProgressRow create(
    1: common.Credentials creds,
    2: required  string key,
    3: list<common.DataField> fields
  )
  SyncProgressRow.SyncProgressRow remove(
    1: common.Credentials creds,
    2: required  string key
  )
  SyncProgressRow.SyncProgressRow update(
    1: common.Credentials creds,
    2: required  string key,
    3: list<common.DataField> fields
  )
}
