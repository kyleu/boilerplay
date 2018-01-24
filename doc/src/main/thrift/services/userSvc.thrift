namespace java services.audit

include "../common.thrift"
include "../result.thrift"
include "../models/user.thrift"

service SystemUserService {
  user.SystemUser getByPrimaryKey(
    1: common.Credentials creds,
    2: common.UUID id
  );
  list<user.SystemUser> getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: list<common.UUID> idSeq
  );

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  );
  list<user.SystemUser> getAll(
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
  list<user.SystemUser> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  );
  list<user.SystemUser> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  );

  common.int countByAuthor(
    1: common.Credentials creds,
    2: common.UUID author
  )
  list<user.SystemUser> getByAuthor(
    1: common.Credentials creds,
    2: common.UUID author,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  );
  list<user.SystemUser> getByAuthorSeq(
    1: common.Credentials creds,
    2: list<common.UUID> authorSeq
  );

  user.SystemUser insert(
    1: common.Credentials creds,
    2: user.SystemUser model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: list<user.SystemUser> models
  )
  user.SystemUser create(
    1: common.Credentials creds,
    2: list<common.DataField> fields
  )

  user.SystemUser remove(
    1: common.Credentials creds,
    2: common.UUID id
  )
  user.SystemUser update(
    1: common.Credentials creds,
    2: common.UUID id,
    3: list<common.DataField> fields
  )
}
