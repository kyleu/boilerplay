// Generated File
namespace java services.audit

include "../../common.thrift"
include "../../result.thrift"
include "../../models/audit/AuditRow.thrift"

service AuditRowService {
  AuditRow.AuditRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  AuditRow.AuditRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<AuditRow.AuditRow> getAll(
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
  list<AuditRow.AuditRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<AuditRow.AuditRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  AuditRow.AuditRow insert(
    1: common.Credentials creds,
    2: required AuditRow.AuditRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<AuditRow.AuditRow> models
  )
  AuditRow.AuditRow create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  AuditRow.AuditRow remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  AuditRow.AuditRow update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
