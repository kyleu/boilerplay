// Generated File
namespace java services.note

include "../../common.thrift"
include "../../result.thrift"
include "../../models/note/NoteRow.thrift"

service NoteRowService {
  NoteRow.NoteRow getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  NoteRow.NoteRow getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<NoteRow.NoteRow> getAll(
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
  list<NoteRow.NoteRow> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<NoteRow.NoteRow> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  NoteRow.NoteRow insert(
    1: common.Credentials creds,
    2: required NoteRow.NoteRow model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<NoteRow.NoteRow> models
  )
  NoteRow.NoteRow create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  NoteRow.NoteRow remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  NoteRow.NoteRow update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
