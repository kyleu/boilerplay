// Generated File
namespace java services.sync

include "../../common.thrift"
include "../../result.thrift"
include "../../models/sync/SyncProgress.thrift"

service SyncProgressService {
  SyncProgress.SyncProgress getByPrimaryKey(
    1: common.Credentials creds,
    2: required string key
  )
  SyncProgress.SyncProgress getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<string> key
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<SyncProgress.SyncProgress> getAll(
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
  list<SyncProgress.SyncProgress> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<SyncProgress.SyncProgress> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  SyncProgress.SyncProgress insert(
    1: common.Credentials creds,
    2: required SyncProgress.SyncProgress model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<SyncProgress.SyncProgress> models
  )
  SyncProgress.SyncProgress create(
    1: common.Credentials creds,
    2: required  string key,
    3: list<common.DataField> fields
  )
  SyncProgress.SyncProgress remove(
    1: common.Credentials creds,
    2: required  string key
  )
  SyncProgress.SyncProgress update(
    1: common.Credentials creds,
    2: required  string key,
    3: list<common.DataField> fields
  )
}
