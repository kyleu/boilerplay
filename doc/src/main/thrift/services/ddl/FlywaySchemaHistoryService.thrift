// Generated File
namespace java services.ddl

include "../../common.thrift"
include "../../result.thrift"
include "../../models/ddl/FlywaySchemaHistory.thrift"

service FlywaySchemaHistoryService {
  FlywaySchemaHistory.FlywaySchemaHistory getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.long installedRank
  )
  FlywaySchemaHistory.FlywaySchemaHistory getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.long> installedRank
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<FlywaySchemaHistory.FlywaySchemaHistory> getAll(
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
  list<FlywaySchemaHistory.FlywaySchemaHistory> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<FlywaySchemaHistory.FlywaySchemaHistory> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  FlywaySchemaHistory.FlywaySchemaHistory insert(
    1: common.Credentials creds,
    2: required FlywaySchemaHistory.FlywaySchemaHistory model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<FlywaySchemaHistory.FlywaySchemaHistory> models
  )
  FlywaySchemaHistory.FlywaySchemaHistory create(
    1: common.Credentials creds,
    2: required  common.long installedRank,
    3: list<common.DataField> fields
  )
  FlywaySchemaHistory.FlywaySchemaHistory remove(
    1: common.Credentials creds,
    2: required  common.long installedRank
  )
  FlywaySchemaHistory.FlywaySchemaHistory update(
    1: common.Credentials creds,
    2: required  common.long installedRank,
    3: list<common.DataField> fields
  )
}
