// Generated File
namespace java services.audit

include "../../common.thrift"
include "../../result.thrift"
include "../../models/audit/AuditRecord.thrift"

service AuditRecordService {
  AuditRecord.AuditRecord getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  AuditRecord.AuditRecord getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<AuditRecord.AuditRecord> getAll(
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
  list<AuditRecord.AuditRecord> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<AuditRecord.AuditRecord> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  AuditRecord.AuditRecord insert(
    1: common.Credentials creds,
    2: required AuditRecord.AuditRecord model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<AuditRecord.AuditRecord> models
  )
  AuditRecord.AuditRecord create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  AuditRecord.AuditRecord remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  AuditRecord.AuditRecord update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
