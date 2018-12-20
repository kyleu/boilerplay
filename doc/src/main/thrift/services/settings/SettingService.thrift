// Generated File
namespace java services.settings

include "../../common.thrift"
include "../../result.thrift"
include "../../models/settings/Setting.thrift"

service SettingService {
  Setting.Setting getByPrimaryKey(
    1: common.Credentials creds,
    2: required SettingKey k
  )
  Setting.Setting getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<SettingKey> k
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<Setting.Setting> getAll(
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
  list<Setting.Setting> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<Setting.Setting> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  Setting.Setting insert(
    1: common.Credentials creds,
    2: required Setting.Setting model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<Setting.Setting> models
  )
  Setting.Setting create(
    1: common.Credentials creds,
    2: required  SettingKey k,
    3: list<common.DataField> fields
  )
  Setting.Setting remove(
    1: common.Credentials creds,
    2: required  SettingKey k
  )
  Setting.Setting update(
    1: common.Credentials creds,
    2: required  SettingKey k,
    3: list<common.DataField> fields
  )
}
