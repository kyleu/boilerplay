namespace java services.audit

include "../common.thrift"
include "../result.thrift"
include "../models/audit.thrift"

service AuditService {
  audit.Audit getByPrimaryKey(
    1: common.Credentials creds,
    2: common.UUID id
  );
  list<audit.Audit> getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: list<common.UUID> idSeq
  );

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  );
  list<audit.Audit> getAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  );

  common.int searchCount(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters
  );
  list<audit.Audit> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  );
  list<audit.Audit> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  );

  common.int countByUserId(
    1: common.Credentials creds,
    2: common.UUID userId
  )
  list<audit.Audit> getByUserId(
    1: common.Credentials creds,
    2: common.UUID id,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  );
  list<audit.Audit> getByUserIdSeq(
    1: common.Credentials creds,
    2: list<common.UUID> idSeq
  );

  audit.Audit insert(
    1: common.Credentials creds,
    2: audit.Audit model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: list<audit.Audit> models
  )
  audit.Audit create(
    1: common.Credentials creds,
    2: list<common.DataField> fields
  )

  audit.Audit remove(
    1: common.Credentials creds,
    2: common.UUID id
  )
  audit.Audit update(
    1: common.Credentials creds,
    2: common.UUID id,
    3: list<common.DataField> fields
  )
}
