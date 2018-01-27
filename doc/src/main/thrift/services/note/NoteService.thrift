// Generated File
namespace java services.note

include "../../common.thrift"
include "../../result.thrift"
include "../../models/note/Note.thrift"

service NoteService {
  Note.Note getByPrimaryKey(
    1: common.Credentials creds,
    2: required common.UUID id
  )
  Note.Note getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: required list<common.UUID> id
  )

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  )
  list<Note.Note> getAll(
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
  list<Note.Note> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  )
  list<Note.Note> searchExact(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  )


  Note.Note insert(
    1: common.Credentials creds,
    2: required Note.Note model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: required list<Note.Note> models
  )
  Note.Note create(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
  Note.Note remove(
    1: common.Credentials creds,
    2: required  common.UUID id
  )
  Note.Note update(
    1: common.Credentials creds,
    2: required  common.UUID id,
    3: list<common.DataField> fields
  )
}
