// Generated File
namespace java services.audit

include "../../common.thrift"
include "../../result.thrift"
include "../../models/audit/AuditRecordRow.thrift"

service AuditRecordRowService {
  AuditRecordRow.AuditRecordRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  AuditRecordRow.AuditRecordRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<AuditRecordRow.AuditRecordRow> getAll(
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
  list<AuditRecordRow.AuditRecordRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<AuditRecordRow.AuditRecordRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  AuditRecordRow.AuditRecordRow insert(
    1: common.Credentials creds,
    2: required AuditRecordRow.AuditRecordRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<AuditRecordRow.AuditRecordRow> models
  )
  AuditRecordRow.AuditRecordRow create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  AuditRecordRow.AuditRecordRow remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  AuditRecordRow.AuditRecordRow update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
