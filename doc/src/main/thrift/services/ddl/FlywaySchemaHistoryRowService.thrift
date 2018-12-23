// Generated File
namespace java services.ddl

include "../../common.thrift"
include "../../result.thrift"
include "../../models/ddl/FlywaySchemaHistoryRow.thrift"

service FlywaySchemaHistoryRowService {
  FlywaySchemaHistoryRow.FlywaySchemaHistoryRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.long installedRank
  )
  FlywaySchemaHistoryRow.FlywaySchemaHistoryRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.long> installedRank
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<FlywaySchemaHistoryRow.FlywaySchemaHistoryRow> getAll(
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
  list<FlywaySchemaHistoryRow.FlywaySchemaHistoryRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<FlywaySchemaHistoryRow.FlywaySchemaHistoryRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  FlywaySchemaHistoryRow.FlywaySchemaHistoryRow insert(
    1: common.Credentials creds,
    2: required FlywaySchemaHistoryRow.FlywaySchemaHistoryRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<FlywaySchemaHistoryRow.FlywaySchemaHistoryRow> models
  )
  FlywaySchemaHistoryRow.FlywaySchemaHistoryRow create(
    1: common.Credentials creds,
    2: required  common.long installedRank,
    3: list<common.DataField> fields
  )
  FlywaySchemaHistoryRow.FlywaySchemaHistoryRow remove(
    1: common.Credentials creds,
    2: required  common.long installedRank
  )
  FlywaySchemaHistoryRow.FlywaySchemaHistoryRow update(
    1: common.Credentials creds,
    2: required  common.long installedRank,
    3: list<common.DataField> fields
  )
}
