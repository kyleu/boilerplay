namespace java services.audit

include "../common.thrift"
include "../result.thrift"
include "../models/note.thrift"

service NoteService {
  note.Note getByPrimaryKey(
    1: common.Credentials creds,
    2: common.UUID id
  );
  list<note.Note> getByPrimaryKeySeq(
    1: common.Credentials creds,
    2: list<common.UUID> idSeq
  );

  common.int countAll(
    1: common.Credentials creds,
    2: list<result.Filter> filters
  );
  list<note.Note> getAll(
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
  list<note.Note> search(
    1: common.Credentials creds,
    2: required string q,
    3: list<result.Filter> filters,
    4: list<result.OrderBy> orderBys,
    5: common.int limit,
    6: common.int offset
  );
  list<note.Note> searchExact(
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
  list<note.Note> getByAuthor(
    1: common.Credentials creds,
    2: common.UUID author,
    3: list<result.OrderBy> orderBys,
    4: common.int limit,
    5: common.int offset
  );
  list<note.Note> getByAuthorSeq(
    1: common.Credentials creds,
    2: list<common.UUID> authorSeq
  );

  note.Note insert(
    1: common.Credentials creds,
    2: note.Note model
  )
  common.int insertBatch(
    1: common.Credentials creds,
    2: list<note.Note> models
  )
  note.Note create(
    1: common.Credentials creds,
    2: list<common.DataField> fields
  )

  note.Note remove(
    1: common.Credentials creds,
    2: common.UUID id
  )
  note.Note update(
    1: common.Credentials creds,
    2: common.UUID id,
    3: list<common.DataField> fields
  )
}
