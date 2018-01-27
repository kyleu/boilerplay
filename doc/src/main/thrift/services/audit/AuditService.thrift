// Generated File
namespace java services.audit

include "../../common.thrift"
include "../../result.thrift"
include "../../models/audit/Audit.thrift"

service AuditService {
  Audit.Audit getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  Audit.Audit getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<Audit.Audit> getAll(
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
  list<Audit.Audit> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<Audit.Audit> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  Audit.Audit insert(
    1: common.Credentials creds,
    2: required Audit.Audit model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<Audit.Audit> models
  )
  Audit.Audit create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  Audit.Audit remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  Audit.Audit update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
